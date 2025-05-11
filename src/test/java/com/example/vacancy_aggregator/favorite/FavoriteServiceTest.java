package com.example.vacancy_aggregator.favorite;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.favorite.vacancy.entity.UserFavorite;
import com.example.vacancy_aggregator.favorite.vacancy.repository.UserFavoriteRepository;
import com.example.vacancy_aggregator.favorite.vacancy.service.FavoriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private UserFavoriteRepository repository;

    @InjectMocks
    private FavoriteService service;

    private User proUser;
    private User freeUser;
    private Vacancy sampleVacancy;

    @BeforeEach
    void setUp() {
        // PRO-пользователь
        proUser = new User();
        proUser.setId(100L);
        // FREE-пользователь (не PRO)
        freeUser = new User();
        freeUser.setId(200L);
        // образец вакансии
        sampleVacancy = Vacancy.builder()
                .source("hh")
                .externalId("v-123")
                .title("Dev")
                .company("X")
                .city("Moscow")
                .salaryFrom(100)
                .salaryTo(200)
                .currency("RUR")
                .description("desc")
                .experienceReq("1-3")
                .employmentType("full")
                .schedule("flex")
                .publishedAt(OffsetDateTime.now())
                .url("http://...")
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Устанавливает в SecurityContext аутентификацию
     * с ролью ROLE_PRO (isPro=true) или ROLE_USER (isPro=false).
     */
    private void authAs(String username, boolean isPro) {
        var role = isPro ? "ROLE_PRO" : "ROLE_USER";
        var auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }



    @Test
    @DisplayName("like() — PRO пользователь, нет существующего фаворита → сохраняем")
    void like_proUser_savesFavorite() {
        authAs("pro", true);
        when(repository.findByUserIdAndSourceAndExternalId(
                proUser.getId(), "hh", "v-123"))
                .thenReturn(Optional.empty());

        service.like(proUser, sampleVacancy);

        verify(repository, times(1))
                .save(ArgumentCaptor.forClass(UserFavorite.class).capture());
    }

    @Test
    @DisplayName("like() — FREE пользователь, нет существующего фаворита → сохраняем")
    void like_freeUser_savesFavorite() {
        authAs("free", false);
        when(repository.findByUserIdAndSourceAndExternalId(
                freeUser.getId(), "hh", "v-123"))
                .thenReturn(Optional.empty());

        service.like(freeUser, sampleVacancy);

        verify(repository, times(1))
                .save(ArgumentCaptor.forClass(UserFavorite.class).capture());
    }

    @Test
    @DisplayName("like() — уже есть фаворит → ничего не сохраняем")
    void like_existingFavorite_noSave() {
        authAs("any", false);
        // эмулируем, что фаворит уже есть
        when(repository.findByUserIdAndSourceAndExternalId(
                freeUser.getId(), "hh", "v-123"))
                .thenReturn(Optional.of(new UserFavorite()));

        service.like(freeUser, sampleVacancy);

        verify(repository, never()).save(any());
    }


    @Test
    @DisplayName("unlike() — PRO пользователь → удаляем по ключу")
    void unlike_proUser_callsDelete() {
        authAs("pro", true);

        service.unlike(proUser, "hh", "v-123");

        verify(repository, times(1))
                .deleteByUserIdAndSourceAndExternalId(
                        proUser.getId(), "hh", "v-123");
    }

    @Test
    @DisplayName("unlike() — FREE пользователь → удаляем по ключу")
    void unlike_freeUser_callsDelete() {
        authAs("free", false);

        service.unlike(freeUser, "hh", "v-123");

        verify(repository, times(1))
                .deleteByUserIdAndSourceAndExternalId(
                        freeUser.getId(), "hh", "v-123");
    }

    @Test
    @DisplayName("list() — PRO пользователь, репозиторий пуст → возвращаем пустой список")
    void list_proUser_emptyRepo_returnsEmpty() {
        authAs("pro", true);
        when(repository.findAllByUserId(proUser.getId()))
                .thenReturn(List.of());

        var result = service.list(proUser);

        assertThat(result).isEmpty();
        verify(repository, times(1))
                .findAllByUserId(proUser.getId());
    }

    @Test
    @DisplayName("list() — FREE пользователь, репозиторий пуст → возвращаем пустой список")
    void list_freeUser_emptyRepo_returnsEmpty() {
        authAs("free", false);
        when(repository.findAllByUserId(freeUser.getId()))
                .thenReturn(List.of());

        var result = service.list(freeUser);

        assertThat(result).isEmpty();
        verify(repository, times(1))
                .findAllByUserId(freeUser.getId());
    }

}