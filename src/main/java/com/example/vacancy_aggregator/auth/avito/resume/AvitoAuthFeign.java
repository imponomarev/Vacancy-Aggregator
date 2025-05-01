package com.example.vacancy_aggregator.auth.avito.resume;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "avitoAuth", url = "${avito.api.base-url}")
public interface AvitoAuthFeign {

    @PostMapping(value = "/token/")
    AvitoTokenResponse token(@RequestParam("grant_type") String grantType,
                             @RequestParam("client_id") String clientId,
                             @RequestParam("client_secret") String clientSecret
    );

    record AvitoTokenResponse(@JsonProperty("access_token") String accessToken,
                              @JsonProperty("expires_in") long expiresIn) {
    }
}
