package com.example.vacancy_aggregator.dto.sj;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SjResumeSearchResponse(
        @JsonProperty("objects") Item[] objects,
        int total
) {
    public record Item(
            @JsonProperty("id") long id,
            @JsonProperty("profession") String position,
            @JsonProperty("town") Town town,
            @JsonProperty("payment_to") Integer salary,
            @JsonProperty("currency") String currency,
            @JsonProperty("updated_at") long updatedAt
    ) {
        public record Town(String title) {
        }
    }
}
