package com.example.vacancy_aggregator.controller;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.auth.security.JwtFilter;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.favorite.vacancy.service.FavoriteService;
import com.example.vacancy_aggregator.favorite.vacancy.web.FavoriteController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "u@example.com", roles = {"USER"})
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService service;

    @MockBean
    private UserRepository users;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    @DisplayName("POST /favorites — like")
    void like() throws Exception {
        // Given
        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setEmail("u@example.com");
        when(users.findByEmail("u@example.com"))
                .thenReturn(Optional.of(fakeUser));

        String vacancyJson = """
            {
              "source":"hh",
              "externalId":"42",
              "title":"Dev",
              "company":"X",
              "city":"Moscow",
              "salaryFrom":100,
              "salaryTo":200,
              "currency":"RUR",
              "description":"d",
              "experienceReq":"1-3",
              "employmentType":"full",
              "schedule":"flex",
              "publishedAt":"2025-05-10T12:00:00Z",
              "url":"http://..."
            }
            """;

        // When & Then
        mockMvc.perform(post("/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vacancyJson))
                .andExpect(status().isOk());

        // Verify service was called with proper arguments
        ArgumentCaptor<Vacancy> captor = ArgumentCaptor.forClass(Vacancy.class);
        verify(service).like(eq(fakeUser), captor.capture());
        assertThat(captor.getValue().getExternalId()).isEqualTo("42");
    }

    @Test
    @DisplayName("DELETE /favorites/{source}/{id} — unlike")
    void unlike() throws Exception {
        // Given
        User fakeUser = new User();
        fakeUser.setId(2L);
        fakeUser.setEmail("u@example.com");
        when(users.findByEmail("u@example.com"))
                .thenReturn(Optional.of(fakeUser));

        // When & Then
        mockMvc.perform(delete("/favorites/hh/42"))
                .andExpect(status().isOk());

        verify(service).unlike(fakeUser, "hh", "42");
    }

    @Test
    @DisplayName("GET /favorites — list")
    void list() throws Exception {
        // Given
        User fakeUser = new User();
        fakeUser.setId(3L);
        fakeUser.setEmail("u@example.com");
        when(users.findByEmail("u@example.com"))
                .thenReturn(Optional.of(fakeUser));

        Vacancy v = Vacancy.builder()
                .externalId("99")
                .source("sj")
                .build();
        when(service.list(fakeUser))
                .thenReturn(List.of(v));

        // When & Then
        mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].externalId").value("99"));

        verify(service).list(fakeUser);
    }
}
