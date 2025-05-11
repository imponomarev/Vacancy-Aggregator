package com.example.vacancy_aggregator.dto.avito;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Ответ Avito API для поиска вакансий.
 *
 * @param meta      метаданные страницы (номер, общее число, размер)
 * @param vacancies список вакансий
 */
public record AvitoSearchResponse(
        Meta meta,
        List<Item> vacancies
) {
    /**
     * Описание одной вакансии из списка.
     *
     * @param id             идентификатор вакансии
     * @param title          заголовок вакансии
     * @param profession     краткое описание (профессия)
     * @param companyName    название компании
     * @param addressDetails вложенный объект с адресом и городом
     * @param published_at   дата публикации в строковом формате ISO
     * @param link           ссылка на вакансию
     */
    public record Item(
            String id,
            String title,
            String profession,
            String companyName,
            AddressDetails addressDetails,
            @JsonProperty("published_at") String published_at,
            String link) {

        /**
         * Вложенный класс с деталями адреса.
         *
         * @param address полное текстовое описание адреса
         * @param city    название города
         */
        public record AddressDetails(String address, String city) {
        }
    }

    /**
     * Метаданные ответа:
     *
     * @param page     текущая страница
     * @param pages    всего страниц
     * @param per_page элементов на странице
     */
    public record Meta(int page, int pages, int per_page) {
    }
}
