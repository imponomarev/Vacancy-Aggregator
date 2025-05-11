package com.example.vacancy_aggregator.client.avito;

import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse;
import feign.config.avito.AvitoVacancyFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для Avito API вакансий.
 */
@FeignClient(
        name = "avito",
        url = "${avito.api.base-url}",
        configuration = AvitoVacancyFeignConfig.class)
public interface AvitoFeign {

    /**
     * GET /job/v2/vacancies
     *
     * @param text     ключевые слова для поиска
     * @param page     номер страницы
     * @param perPage  число элементов на странице
     * @param regionId числовой ID региона
     * @return {@link AvitoSearchResponse} с результатами поиска
     */
    @GetMapping("/job/v2/vacancies")
    AvitoSearchResponse search(@RequestParam("text") String text,
                               @RequestParam("page") int page,
                               @RequestParam("per_page") int perPage,
                               @RequestParam("region_id") int regionId
    );
}