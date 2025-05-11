package com.example.vacancy_aggregator.client.sj;

import com.example.vacancy_aggregator.dto.sj.SjResumeSearchResponse;
import feign.config.sj.SjResumeFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для SuperJob Resume API.
 */
@FeignClient(name = "sjResumes", url = "${sj.api.resume-url}", configuration = SjResumeFeignConfig.class)
public interface SjResumeFeign {

    @GetMapping("/resumes")
    SjResumeSearchResponse search(@RequestParam String keyword,
                                  @RequestParam int page,
                                  @RequestParam int count,
                                  @RequestParam(value = "payment_from", required = false) Integer paymentFrom,
                                  @RequestParam(value = "payment_to", required = false) Integer salaryTo,
                                  @RequestParam(value = "age_from", required = false) Integer ageFrom,
                                  @RequestParam(value = "age_to", required = false) Integer ageTo,
                                  @RequestParam(value = "experience_from", required = false) Integer experienceFrom,
                                  @RequestParam(value = "experience_to", required = false) Integer experienceTo,
                                  @RequestParam(value = "type_of_work", required = false) Integer schedule,
                                  @RequestParam(value = "education", required = false) Integer education

    );
}