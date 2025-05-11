package com.example.vacancy_aggregator.client.sj;

import com.example.vacancy_aggregator.location.dto.SjTownResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для SuperJob API справочника городов.
 */
@FeignClient(name = "sjDict", url = "${sj.api.base-url}")
public interface SjDictFeign {

    /**
     * Получить список городов по части названия.
     *
     * @param q   подстрока названия
     * @param all признак «вернуть все»
     * @return {@link SjTownResponse} со списком городов
     */
    @GetMapping("/2.0/towns")
    SjTownResponse towns(@RequestParam("keyword") String q,
                         @RequestParam("all") int all);
}