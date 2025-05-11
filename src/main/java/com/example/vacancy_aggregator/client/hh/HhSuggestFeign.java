package com.example.vacancy_aggregator.client.hh;

import com.example.vacancy_aggregator.location.dto.SuggestAreaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для HH-Suggest API — поиска регионов по тексту.
 * Используется при разрешении человекочитаемых названий
 * во внутренние коды region_id для запросов вакансий.
 */
@FeignClient(name = "hhSuggest", url = "${hh.api.base-url}")
public interface HhSuggestFeign {

    /**
     * Возвращает подсказки по регионам.
     *
     * @param text   часть названия региона на латинице или кириллице
     * @param locale язык результатов
     * @return DTO {@link SuggestAreaResponse} с массивом элементов {id, text}
     */
    @GetMapping("/suggests/areas")
    SuggestAreaResponse suggest(@RequestParam String text, @RequestParam("locale") String locale);
}
