package com.example.vacancy_aggregator.location.dto;

import java.util.List;

public record SuggestAreaResponse(
        List<Item> items) {

    public record Item(
            String id,
            String text,
            String parent_id) {}
}
