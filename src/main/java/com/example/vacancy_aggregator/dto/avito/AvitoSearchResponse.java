package com.example.vacancy_aggregator.dto.avito;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AvitoSearchResponse(
        Meta meta,
        List<Item> vacancies
) {
    public record Item(
            String id,
            String title,
            String profession,
            String companyName,
            AddressDetails addressDetails,
            @JsonProperty("published_at") String published_at,
            String link) {

        public record AddressDetails(String address, String city) {
        }
    }

    public record Meta(int page, int pages, int per_page) {
    }
}
