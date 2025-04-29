package com.example.vacancy_aggregator.client.sj;

import com.example.vacancy_aggregator.config.sj.SjFeignConfig;
import com.example.vacancy_aggregator.dto.sj.SjSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sj", url = "${sj.api.base-url}", configuration = SjFeignConfig.class)
public interface SjFeign {

    @GetMapping("/2.0/vacancies/")
    SjSearchResponse search(
            @RequestParam("keyword") String text,
            @RequestParam("town") String town,
            @RequestParam("page") int page,
            @RequestParam("count") int perPage);
}
