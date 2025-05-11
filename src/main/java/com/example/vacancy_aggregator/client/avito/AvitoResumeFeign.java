package com.example.vacancy_aggregator.client.avito;

import com.example.vacancy_aggregator.dto.avito.AvitoResumeSearchResponse;
import feign.config.avito.AvitoResumeFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для Avito Resume API.
 */
@FeignClient(name = "avitoResumes",
        url = "${avito.api.base-url}",
        configuration = AvitoResumeFeignConfig.class)
public interface AvitoResumeFeign {

    @GetMapping("/job/v2/resumes")
    AvitoResumeSearchResponse search(@RequestParam("text") String text,
                                     @RequestParam("page") int page,
                                     @RequestParam("per_page") int perPage,
                                     @RequestParam("region_id") int regionId,
                                     @RequestParam(value = "salary_min", required = false) Integer salaryMin,
                                     @RequestParam(value = "salary_max", required = false) Integer salaryMax,
                                     @RequestParam(value = "age_min", required = false) Integer ageMin,
                                     @RequestParam(value = "age_max", required = false) Integer ageMax,
                                     @RequestParam(value = "experience_min", required = false) Integer experienceMin,
                                     @RequestParam(value = "experience_max", required = false) Integer experienceMax,
                                     @RequestParam(value = "schedule", required = false) String schedule,
                                     @RequestParam(value = "education_level", required = false) String educationLevel
    );
}
