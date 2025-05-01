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
                                  @RequestParam("per_page") int perPage);
}
