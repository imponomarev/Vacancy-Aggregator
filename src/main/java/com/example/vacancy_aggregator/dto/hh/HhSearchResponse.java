package com.example.vacancy_aggregator.dto.hh;

import java.util.List;
import java.util.Map;

/**
 * Ответ от HH API при поиске вакансий.
 *
 * @param found общее число найденных вакансий
 * @param pages число страниц в выдаче
 * @param items список вакансий на текущей странице
 */
public record HhSearchResponse(
        int found,
        int pages,
        List<HhVacancyItem> items
) {
    /**
     * Один элемент вакансии из ответа HH.
     *
     * @param id            внутренний (строковый) идентификатор вакансии
     * @param name          заголовок вакансии
     * @param salary        мапа с ключами "from", "to", "currency", "gross"
     * @param employer      мапа с данными работодателя, в том числе "name" и "alternate_url"
     * @param area          мапа с данными региона: "id", "name"
     * @param experience    мапа с данными опыта: "id", "name"
     * @param employment    мапа с данными типа занятости: "id", "name"
     * @param schedule      мапа с данными графика работы: "id", "name"
     * @param snippet       мапа с «сниппетом»: ключи "responsibility" и/или "requirement"
     * @param published_at  дата публикации в формате ISO_OFFSET_DATE_TIME
     * @param alternate_url URL страницы вакансии на сайте hh.ru
     */
    public record HhVacancyItem(
            String id,
            String name,
            Map<String, Object> salary,
            Map<String, Object> employer,
            Map<String, Object> area,
            Map<String, Object> experience,
            Map<String, Object> employment,
            Map<String, Object> schedule,
            Map<String, Object> snippet,
            String published_at,
            String alternate_url
    ) {
    }
}
