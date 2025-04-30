package com.example.vacancy_aggregator.client.sj;

import com.example.vacancy_aggregator.dto.sj.SjResumeSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="sjResumes", url="${sj.api.resume-url}")
public interface SjResumeFeign {

    @GetMapping("/resumes")
    SjResumeSearchResponse search(@RequestParam String keyword,
                                  @RequestParam int page,
                                  @RequestParam int count,
                                  @RequestHeader("X-Api-App-Id") String appId,
                                  @RequestHeader("Authorization") String bearer);
}