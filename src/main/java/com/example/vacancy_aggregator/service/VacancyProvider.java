package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import java.util.List;

public interface VacancyProvider {
    String providerName();
    List<Vacancy> search(VacancyQuery query);
    Vacancy getById(String externalId);
}
