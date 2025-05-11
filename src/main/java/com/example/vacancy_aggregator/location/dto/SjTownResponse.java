package com.example.vacancy_aggregator.location.dto;

import java.util.List;

/**
 * Ответ SuperJob API /2.0/towns для списка городов.
 *
 * @param objects список элементов {@link Item}
 */
public record SjTownResponse(
        List<Item> objects) {

    /**
     * Один элемент справочника городов SuperJob.
     *
     * @param id        числовой ID города
     * @param title     название города
     * @param parent_id ID родительского региона
     */
    public record Item(
            long id,
            String title,
            Long parent_id) {
    }
}
