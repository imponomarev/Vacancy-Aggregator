package com.example.vacancy_aggregator.dto.avito;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AvitoSearchResponse(
        @JsonProperty("status") String status,
        @JsonProperty("result") Item[] result,
        @JsonProperty("total") int total
) {
    public record Item(
            String id,
            String title,
            Employer employer,
            Location location,
            @JsonProperty("salary_min") Integer salaryMin,
            @JsonProperty("salary_max") Integer salaryMax,
            String currency,
            @JsonProperty("published_at") String publishedAt
    ) {
    }

    public record Employer(String name) {
    }

    public record Location(String city) {
    }
}
