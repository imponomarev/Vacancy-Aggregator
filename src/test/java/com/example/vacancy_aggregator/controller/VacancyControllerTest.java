package com.example.vacancy_aggregator.controller;

import com.example.vacancy_aggregator.auth.security.JwtFilter;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.service.VacancyQuery;
import com.example.vacancy_aggregator.service.impl.vacancy.VacancySearchService;
import com.example.vacancy_aggregator.service.util.ExperienceLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тест контроллера /vacancies.
 * Фильтры безопасности полностью отключены из-за мокирования JwtFilter.
 */
@WebMvcTest(VacancyController.class)
@AutoConfigureMockMvc(addFilters = false)
class VacancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacancySearchService service;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    @DisplayName("GET /vacancies — возвращает список вакансий с фильтрами")
    void search_withParams_returnsVacancies() throws Exception {
        // подготовка тестовой вакансии
        Vacancy v = Vacancy.builder()
                .source("hh")
                .externalId("123")
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
                .publishedAt(OffsetDateTime.parse("2025-05-10T12:00:00Z"))
                .url("http://...")
                .build();

        // мокаем сервис
        when(service.search(any())).thenReturn(List.of(v));

        // выполняем GET-запрос
        mockMvc.perform(get("/vacancies")
                        .param("text", "java")
                        .param("area", "Moscow")
                        .param("page", "0")
                        .param("perPage", "5")
                        .param("providers", "hh,sj")
                        .param("salaryFrom", "100")
                        .param("salaryTo", "200")
                        .param("experience", "BETWEEN_1_AND_3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].source").value("hh"))
                .andExpect(jsonPath("$[0].title").value("Dev"))
                .andExpect(jsonPath("$[0].salaryFrom").value(100));

        // проверяем, что в сервис ушёл правильный запрос
        ArgumentCaptor<VacancyQuery> captor = ArgumentCaptor.forClass(VacancyQuery.class);
        verify(service, times(1)).search(captor.capture());
        VacancyQuery q = captor.getValue();
        assertThat(q.text()).isEqualTo("java");
        assertThat(q.area()).isEqualTo("Moscow");
        assertThat(q.page()).isEqualTo(0);
        assertThat(q.perPage()).isEqualTo(5);
        assertThat(q.providers()).containsExactly("hh", "sj");
        assertThat(q.salaryFrom()).isEqualTo(100);
        assertThat(q.salaryTo()).isEqualTo(200);
        assertThat(q.experience()).isEqualTo(ExperienceLevel.BETWEEN_1_AND_3);
    }
}
