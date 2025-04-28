package com.example.vacancy_aggregator.dto.avito;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Ответ Avito /items/{id} */
public record AvitoItemResponse(
        String id,
        String title,
        Employer employer,
        Location location,
        @JsonProperty("salary_min") Integer salaryMin,
        @JsonProperty("salary_max") Integer salaryMax,
        String currency,
        @JsonProperty("published_at") String publishedAt,
        String description
) {
    public record Employer(String name) {}
    public record Location(String city) {}
}
