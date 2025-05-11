package com.example.vacancy_aggregator.location.dto;

import java.util.List;

/**
 * Ответ HH API /suggests/areas для подсказок регионов.
 *
 * @param items список подсказок {@link Item}
 */
public record SuggestAreaResponse(
        List<Item> items) {

    /**
     * Одна подсказка региона HH.
     *
     * @param id        строковый идентификатор региона
     * @param text      человекочитаемый текст названия
     * @param parent_id строковый ID родительского региона
     */
    public record Item(
            String id,
            String text,
            String parent_id) {
    }
}
