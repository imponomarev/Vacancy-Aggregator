package com.example.vacancy_aggregator.controller;

import com.example.vacancy_aggregator.auth.security.JwtFilter;
import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.service.impl.resume.ResumeSearchService;
import com.example.vacancy_aggregator.service.ResumeQuery;
import com.example.vacancy_aggregator.service.util.ResumeExperience;
import com.example.vacancy_aggregator.service.util.ResumeSchedule;
import com.example.vacancy_aggregator.service.util.ResumeEducation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ResumeController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResumeSearchService service;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    @DisplayName("GET /resumes — возвращает список резюме с фильтрами")
    void search_withParams_returnsResumes() throws Exception {
        Resume r = new Resume(
                "sj", "777", "Ivan", "Petrov", "Dev",
                150000, "RUR", "Moscow",
                OffsetDateTime.parse("2025-05-10T12:00:00Z"),
                "http://...", 30, 36, "male",
                "Higher", List.of()
        );
        when(service.search(any())).thenReturn(List.of(r));

        mockMvc.perform(get("/resumes")
                        .param("text", "python")
                        .param("area", "Moscow")
                        .param("page", "0")
                        .param("perPage", "10")
                        .param("salaryFrom", "100000")
                        .param("salaryTo", "200000")
                        .param("ageFrom", "25")
                        .param("ageTo", "40")
                        .param("experience", ResumeExperience.BETWEEN_3_AND_6_YEARS.name())
                        .param("schedule", ResumeSchedule.REMOTE.name())
                        .param("education", ResumeEducation.HIGHER.name())
                        .param("providers", "hh,sj"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].externalId").value("777"))
                .andExpect(jsonPath("$[0].age").value(30));

        ArgumentCaptor<ResumeQuery> captor = ArgumentCaptor.forClass(ResumeQuery.class);
        verify(service).search(captor.capture());
        ResumeQuery q = captor.getValue();
        assertThat(q.text()).isEqualTo("python");
        assertThat(q.salaryFrom()).isEqualTo(100000);
        assertThat(q.ageTo()).isEqualTo(40);
        assertThat(q.experience().name()).isEqualTo("BETWEEN_3_AND_6_YEARS");
    }
}
