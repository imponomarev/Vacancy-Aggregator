package com.example.vacancy_aggregator.client.hh;

import com.example.vacancy_aggregator.dto.HhSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hh", url = "${hh.api.base-url}")
public interface HhFeign {

    @GetMapping("/vacancies")
    HhSearchResponse search(@RequestParam String text,
                            @RequestParam String area,
                            @RequestParam int    page,
                            @RequestParam("per_page") int perPage);
}
