package com.example.vacancy_aggregator.service;

import java.util.List;

public record VacancyQuery(
        String text,
        Integer page,
        Integer perPage,
        String area,
        List<String> providers
) {
}
