package com.example.vacancy_aggregator.dto.avito;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AvitoResumeSearchResponse(
        Meta meta,
        @JsonProperty("resumes") Item[] resumes
) {
    public record Item(
            String id,
            String title,
            @JsonProperty("salary") Integer salary,
            Location location,
            @JsonProperty("updated") String updatedAt,
            Integer age,
            @JsonProperty("total_experience") Integer totalExperience,
            String gender,
            @JsonProperty("education_level") String educationLevel
    ) {
        public record Location(String title) {
        }
    }

    public record Meta(int page, int pages, int per_page, long cursor) {
    }
}