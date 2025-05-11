package com.example.vacancy_aggregator.favorite;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.favorite.resume.entity.UserResumeFavorite;
import com.example.vacancy_aggregator.favorite.resume.repository.UserResumeFavoriteRepository;
import com.example.vacancy_aggregator.favorite.resume.service.ResumeFavoriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeFavoriteServiceTest {

    @Mock
    private UserResumeFavoriteRepository repository;

    @InjectMocks
    private ResumeFavoriteService service;

    private User proUser;
    private User normalUser;
    private Resume sampleResume;

    @BeforeEach
    void setUp() {
        proUser = new User();
        proUser.setId(10L);

        normalUser = new User();
        normalUser.setId(20L);

        sampleResume = new Resume(
                "hh",
                "r-456",
                "Ivan", "Ivanov",
                "Developer",
                150_000,
                "RUR",
                "Moscow",
                OffsetDateTime.now(),
                "https://...",
                30,
                60,
                "male",
                "higher",
                List.of() // пустой опыт
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void authAs(boolean isPro) {
        var auth = new UsernamePasswordAuthenticationToken(
                isPro ? "PRO" : "USER",
                null,
                List.of(new SimpleGrantedAuthority(isPro ? "ROLE_PRO" : "ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("like() — PRO → сохраняем новую сущность")
    void like_pro_savesEntity() {
        authAs(true);
        when(repository.findByUserIdAndSourceAndExternalId(
                proUser.getId(), "hh", "r-456"))
                .thenReturn(Optional.empty());

        service.like(proUser, sampleResume);

        verify(repository, times(1))
                .save(ArgumentCaptor.forClass(UserResumeFavorite.class).capture());
    }

    @Test
    @DisplayName("like() — НЕ-PRO → AccessDeniedException")
    void like_nonPro_throws() {
        authAs(false);
        assertThrows(AccessDeniedException.class,
                () -> service.like(normalUser, sampleResume));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("unlike() — PRO → удаляем по ключу")
    void unlike_pro_callsDelete() {
        authAs(true);

        service.unlike(proUser, "hh", "r-456");

        verify(repository, times(1))
                .deleteByUserIdAndSourceAndExternalId(proUser.getId(), "hh", "r-456");
    }

    @Test
    @DisplayName("unlike() — НЕ-PRO → AccessDeniedException")
    void unlike_nonPro_throws() {
        authAs(false);
        assertThrows(AccessDeniedException.class,
                () -> service.unlike(normalUser, "hh", "r-456"));
        verify(repository, never()).deleteByUserIdAndSourceAndExternalId(anyLong(), any(), any());
    }

    @Test
    @DisplayName("list() — PRO, пусто → возвращаем пустой")
    void list_pro_empty() {
        authAs(true);
        when(repository.findAllByUserId(proUser.getId()))
                .thenReturn(List.of());

        var result = service.list(proUser);

        assertThat(result).isEmpty();
        verify(repository).findAllByUserId(proUser.getId());
    }

    @Test
    @DisplayName("list() — НЕ-PRO → AccessDeniedException")
    void list_nonPro_throws() {
        authAs(false);
        assertThrows(AccessDeniedException.class,
                () -> service.list(normalUser));
        verify(repository, never()).findAllByUserId(anyLong());
    }
}