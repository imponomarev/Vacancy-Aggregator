package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.service.util.ExperienceLevel;

import java.util.List;

public record VacancyQuery(
        String text,
        Integer page,
        Integer perPage,
        String area,
        List<String> providers,
        Integer salaryFrom,
        Integer salaryTo,
        ExperienceLevel experience
) {
}
