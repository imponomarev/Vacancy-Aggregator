package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.service.util.ExperienceLevel;

import java.util.List;

/**
 * Параметры поиска вакансий, собранные контроллером.
 *
 * @param text       ключевые слова для поиска
 * @param page       номер страницы
 * @param perPage    элементов на странице
 * @param area       строковое представление региона
 * @param providers  список провайдеров; при null или пустом — все
 * @param salaryFrom минимальная зарплата для фильтрации
 * @param salaryTo   максимальная зарплата для фильтрации
 * @param experience требуемый уровень опыта
 */
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
