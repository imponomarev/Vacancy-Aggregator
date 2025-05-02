package com.example.vacancy_aggregator.dto.hh;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


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
            @JsonProperty("last_name") String lastName,
            Integer age,
            @JsonProperty("total_experience") TotalExp totalExperience,
            @JsonProperty("alternate_url") String url,
            Gender gender,
            Education education,
            List<Experience> experience
    ) {
    }

    public record Area(String name) {
    }

    public record Salary(Integer amount, String currency) {
    }

    public record TotalExp(Integer months) {
    }

    public record Gender(String id, String name) {
    }

    public record Education(Level level) {
        public record Level(String id, String name) {
        }
    }


    public record Experience(
            String company,
            String position,
            String start,
            String end,
            String description
    ) {}

}
