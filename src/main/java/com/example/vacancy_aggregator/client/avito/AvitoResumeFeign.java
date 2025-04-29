package com.example.vacancy_aggregator.client.avito;

import com.example.vacancy_aggregator.dto.avito.AvitoResumeSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "avitoResumes",
        url = "${avito.api.base-url}",
        configuration = com.example.vacancy_aggregator.config.avito.AvitoFeignConfig.class)
public interface AvitoResumeFeign {

    @GetMapping("/job/v2/resumes")
    AvitoResumeSearchResponse search(@RequestParam("text") String text,
                                     @RequestParam("page") int page,
                                     @RequestParam("per_page") int perPage,
                                     @RequestParam("region_id") int regionId,
                                     @RequestHeader("Authorization") String bearer);
}
