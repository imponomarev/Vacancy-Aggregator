package com.example.vacancy_aggregator.dto.avito;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AvitoResumeSearchResponse(
        String status,
        @JsonProperty("result") Item[] result,
        int total
) {
    public record Item(
            String id,
            String title,
            @JsonProperty("salary") Integer salary,
            String currency,
            Location location,
            @JsonProperty("updated_at") String updatedAt
    ) {
        public record Location(String city) {
        }
    }
}