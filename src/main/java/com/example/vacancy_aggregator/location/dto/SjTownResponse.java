package com.example.vacancy_aggregator.location.dto;

import java.util.List;

public record SjTownResponse(
        List<Item> objects) {

    public record Item(
            long id,
            String title,
            Long parent_id) {}
}
