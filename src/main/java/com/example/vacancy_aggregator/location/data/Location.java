package com.example.vacancy_aggregator.location.data;

/**
 * Данные о локации, полученные из разных API:
 *
 * @param hhId    строковый HH-region-id
 * @param sjId    числовой SuperJob-town-id
 * @param avitoId числовой Avito-region-id
 * @param name    исходное название региона
 */
public record Location(
        String hhId,
        Long sjId,
        Integer avitoId,
        String name) {

}