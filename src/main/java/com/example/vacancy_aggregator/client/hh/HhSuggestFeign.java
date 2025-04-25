package com.example.vacancy_aggregator.client.hh;

import com.example.vacancy_aggregator.location.dto.SuggestAreaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="hhSuggest", url="${hh.api.base-url}")
public interface HhSuggestFeign {
    @GetMapping("/suggests/areas")
    SuggestAreaResponse suggest(@RequestParam String text, @RequestParam("locale") String locale);
}
