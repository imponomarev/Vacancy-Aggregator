package com.example.vacancy_aggregator.dto.sj;

import java.util.List;
import java.util.Map;

/**
 * DTO-ответ от SuperJob API при поиске вакансий.
 *
 * @param total   общее число найденных записей
 * @param objects список элементов {@link SjVacancy}
 */
public record SjSearchResponse(int total, List<SjVacancy> objects) {

    /**
     * Описание одной вакансии из SuperJob.
     *
     * @param id             строковый ID вакансии
     * @param profession     название вакансии
     * @param payment_from   минимальная предлагаемая оплата
     * @param payment_to     максимальная предлагаемая оплата
     * @param currency       валюта
     * @param firm_name      название компании
     * @param type_of_work   мапа с данными о типе занятости: "id", "title"
     * @param experience     мапа с данными об опыте: "id", "title"
     * @param town           вложенный объект {@link Town} с ID и названием города
     * @param work           текст описания обязанностей/требований
     * @param date_published unix-timestamp публикации
     * @param link           URL вакансии на SuperJob
     */
    public record SjVacancy(
            String id,
            String profession,
            Integer payment_from,
            Integer payment_to,
            String currency,
            String firm_name,
            Map<String, String> type_of_work,
            Map<String, String> experience,
            Town town,
            String work,
            long date_published,
            String link) {
        public record Town(Integer id, String title) {
        }
    }
}

