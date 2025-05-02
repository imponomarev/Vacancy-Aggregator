package com.example.vacancy_aggregator.client.avito;

import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse;
import feign.config.avito.AvitoVacancyFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "avito",
        url = "${avito.api.base-url}",
        configuration = AvitoVacancyFeignConfig.class)
public interface AvitoFeign {

    @GetMapping("/job/v2/vacancies")
    AvitoSearchResponse search(@RequestParam("text") String text,
                               @RequestParam("page") int page,
                               @RequestParam("per_page") int perPage,
                               @RequestParam("region_id") int regionId
    );
}