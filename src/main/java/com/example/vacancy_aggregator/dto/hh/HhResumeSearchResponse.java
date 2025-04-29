package com.example.vacancy_aggregator.dto.hh;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HhResumeSearchResponse(
        @JsonProperty("items") Item[] items,
        @JsonProperty("found") int found,
        @JsonProperty("page") int page,
        @JsonProperty("pages") int pages
) {
    public record Item(
            String id,
            String title,
            Area area,
            @JsonProperty("salary") Salary salary,
            @JsonProperty("updated_at") String updatedAt,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName
    ) {
    }

    public record Area(String name) {
    }

    public record Salary(Integer amount, String currency) {
    }
}
