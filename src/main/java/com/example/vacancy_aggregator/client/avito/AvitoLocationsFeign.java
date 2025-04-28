package com.example.vacancy_aggregator.client.avito;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "avitoLocations",
        url = "https://autoload.avito.ru/format"
)
public interface AvitoLocationsFeign {
    @GetMapping("/Locations.xml")
    String fetchLocationsXml();
}
