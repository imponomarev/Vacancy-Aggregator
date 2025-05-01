package com.example.vacancy_aggregator.client.avito;

import com.example.vacancy_aggregator.dto.avito.AvitoResumeSearchResponse;
import feign.config.avito.AvitoResumeFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "avitoResumes",
        url = "${avito.api.base-url}",
        configuration = AvitoResumeFeignConfig.class)
public interface AvitoResumeFeign {

    @GetMapping("/job/v2/resumes")
    AvitoResumeSearchResponse search(@RequestParam("text") String text,
                                     @RequestParam("page") int page,
                                     @RequestParam("per_page") int perPage,
                                     @RequestParam("region_id") int regionId
    );
}
