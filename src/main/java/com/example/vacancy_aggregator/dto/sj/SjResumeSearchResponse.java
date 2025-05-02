package com.example.vacancy_aggregator.dto.sj;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SjResumeSearchResponse(
        @JsonProperty("objects") Item[] objects,
        int total
) {
    public record Item(
            @JsonProperty("id") long id,
            @JsonProperty("firstname") String firstName,
            @JsonProperty("lastname") String lastName,
            @JsonProperty("profession") String position,
            @JsonProperty("town") Town town,
            @JsonProperty("payment") Integer payment,
            @JsonProperty("currency") String currency,
            @JsonProperty("date_last_modified") long updatedAt,
            Integer age,
            @JsonProperty("experience_month_total") Integer experienceMonthTotal,
            String link,
            Gender gender,
            Education education,
            @JsonProperty("work_history") List<WorkHistory> workHistory
    ) {
        public record Town(String title) {
        }

        public record Gender(@JsonProperty("title") String title) {
        }

        public record Education(String title) {
        }

        public record WorkHistory(
                String name,
                String profession,
                String work,
                Integer monthbeg,
                Integer monthend,
                Integer yearbeg,
                Integer yearend
        ) {
        }
    }
}
