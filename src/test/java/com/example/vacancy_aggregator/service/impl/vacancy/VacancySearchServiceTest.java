package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.service.VacancyProvider;
import com.example.vacancy_aggregator.service.VacancyQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VacancySearchServiceTest {

    @Mock
    private VacancyProvider hhProvider;
    @Mock
    private VacancyProvider sjProvider;
    @Mock
    private VacancyProvider avitoProvider;

    private VacancySearchService service;

    private final Vacancy hhVacancy = Vacancy.builder()
            .source("hh")
            .externalId("1")
            .title("HH Title")
            .build();
    private final Vacancy sjVacancy = Vacancy.builder()
            .source("sj")
            .externalId("2")
            .title("SJ Title")
            .build();
    private final Vacancy avitoVacancy = Vacancy.builder()
            .source("avito")
            .externalId("3")
            .title("Avito Title")
            .build();

    @BeforeEach
    void setUp() {
        // Инициализируем Mockito-моки
        MockitoAnnotations.openMocks(this);
        // Настраиваем имена провайдеров
        when(hhProvider.providerName()).thenReturn("hh");
        when(sjProvider.providerName()).thenReturn("sj");
        when(avitoProvider.providerName()).thenReturn("avito");

        // По умолчанию каждый провайдер возвращает свой единственный элемент
        when(hhProvider.search(any())).thenReturn(List.of(hhVacancy));
        when(sjProvider.search(any())).thenReturn(List.of(sjVacancy));
        when(avitoProvider.search(any())).thenReturn(List.of(avitoVacancy));

        // Создаём сервис с тремя провайдерами
        service = new VacancySearchService(List.of(hhProvider, sjProvider, avitoProvider));
    }

    @Test
    void givenFreeUser_whenNoProvidersFilter_thenExcludeAvito() {
        // Настраиваем безопасность: пользователь без PRO-роли
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("user", "pass", "ROLE_FREE")
        );

        VacancyQuery query = new VacancyQuery("text", 0, 10, "area", null, null, null, null);
        List<Vacancy> result = service.search(query);

        // Должны присутствовать вакансии hh и sj, но не avito
        assertTrue(result.contains(hhVacancy));
        assertTrue(result.contains(sjVacancy));
        assertFalse(result.contains(avitoVacancy));
    }

    @Test
    void givenProUser_whenNoProvidersFilter_thenIncludeAll() {
        // Настраиваем безопасность: пользователь с PRO-ролей
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", "pass", "ROLE_PRO")
        );

        VacancyQuery query = new VacancyQuery("text", 0, 10, "area", null, null, null, null);
        List<Vacancy> result = service.search(query);

        // Все три источника должны присутствовать
        assertTrue(result.containsAll(List.of(hhVacancy, sjVacancy, avitoVacancy)));
    }

    @Test
    void givenFreeUser_withProvidersFilter_thenOnlyRequestedAndAllowed() {
        // ROLE_FREE + запрос только ["sj","avito"]
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("user", "pass", "ROLE_FREE")
        );

        VacancyQuery query = new VacancyQuery("text", 0, 10, "area", List.of("sj", "avito"), null, null, null);
        List<Vacancy> result = service.search(query);

        // "avito" не должен попасть из-за отсутствия PRO, "sj" — должен
        assertFalse(result.contains(hhVacancy));
        assertTrue(result.contains(sjVacancy));
        assertFalse(result.contains(avitoVacancy));
    }

    @Test
    void givenProUser_withProvidersFilter_thenOnlyRequested() {
        // ROLE_PRO + запрос только ["avito"]
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", "pass", "ROLE_PRO")
        );

        VacancyQuery query = new VacancyQuery("text", 0, 10, "area", List.of("avito"), null, null, null);
        List<Vacancy> result = service.search(query);

        // Только "avito" должен остаться
        assertFalse(result.contains(hhVacancy));
        assertFalse(result.contains(sjVacancy));
        assertTrue(result.contains(avitoVacancy));
    }

    @Test
    void givenFreeUser_withEmptyProvidersList_thenExcludeAvito() {
        // ROLE_FREE + явный пустой список фильтров
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("user", "pass", "ROLE_FREE")
        );

        VacancyQuery query = new VacancyQuery("text", 0, 10, "area", List.of(), null, null, null);
        List<Vacancy> result = service.search(query);

        // Списки фильтров пуст — поведение как без фильтрации
        assertTrue(result.contains(hhVacancy));
        assertTrue(result.contains(sjVacancy));
        assertFalse(result.contains(avitoVacancy));
    }
}
