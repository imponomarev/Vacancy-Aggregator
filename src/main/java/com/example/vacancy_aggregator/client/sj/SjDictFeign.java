package com.example.vacancy_aggregator.client.sj;

import com.example.vacancy_aggregator.location.dto.SjTownResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="sjDict", url="${sj.api.base-url}")
public interface SjDictFeign {
    @GetMapping("/2.0/towns")
    SjTownResponse towns(@RequestParam("keyword") String q,
                         @RequestParam("all") int all);
}