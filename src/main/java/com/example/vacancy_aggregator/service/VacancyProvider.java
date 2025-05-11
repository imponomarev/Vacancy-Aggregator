package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;

import java.util.List;

/**
 * Интерфейс поставщика вакансий.
 * Каждая реализация обращается к конкретному внешнему API.
 */
public interface VacancyProvider {

    /**
     * Уникальный идентификатор провайдера: используется для фильтрации.
     *
     * @return название провайдера (например, "hh", "sj", "avito")
     */
    String providerName();

    /**
     * Выполняет поиск вакансий по заданному запросу.
     *
     * @param query параметры поиска
     * @return список DTO {@link Vacancy}
     */
    List<Vacancy> search(VacancyQuery query);
}
