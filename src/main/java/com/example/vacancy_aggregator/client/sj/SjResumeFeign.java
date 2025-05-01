package com.example.vacancy_aggregator.client.sj;

import com.example.vacancy_aggregator.dto.sj.SjResumeSearchResponse;
import feign.config.sj.SjResumeFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sjResumes", url = "${sj.api.resume-url}", configuration = SjResumeFeignConfig.class)
public interface SjResumeFeign {

    @GetMapping("/resumes")
    SjResumeSearchResponse search(@RequestParam String keyword,
                                  @RequestParam int page,
                                  @RequestParam int count

    );
}