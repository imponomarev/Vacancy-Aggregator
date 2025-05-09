package com.example.vacancy_aggregator.client.hh;


import com.example.vacancy_aggregator.dto.hh.HhResumeSearchResponse;
import feign.config.hh.HhResumeFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hhResumes", url = "${hh.api.resume-url}", configuration = HhResumeFeignConfig.class)
public interface HhResumeFeign {

    @GetMapping("/resumes")
    HhResumeSearchResponse search(@RequestParam String text,
                                  @RequestParam String area,
                                  @RequestParam int page,
                                  @RequestParam("per_page") int perPage,
                                  @RequestParam(value = "salary_from", required = false) Integer salaryFrom,
                                  @RequestParam(value = "salary_to", required = false) Integer salaryTo,
                                  @RequestParam(value = "age_from", required = false) Integer ageFrom,
                                  @RequestParam(value = "age_to", required = false) Integer ageTo,
                                  @RequestParam(value = "experience", required = false) String experience,
                                  @RequestParam(value = "schedule", required = false) String schedule,
                                  @RequestParam(value = "education_levels", required = false) String education);
}
