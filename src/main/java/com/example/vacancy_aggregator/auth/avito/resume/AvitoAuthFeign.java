package com.example.vacancy_aggregator.auth.avito.resume;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для получения access_token от Avito (client_credentials).
 */
@FeignClient(name = "avitoAuth", url = "${avito.api.base-url}")
public interface AvitoAuthFeign {

    /**
     * @param grantType    всегда "client_credentials"
     * @param clientId     client_id из настроек
     * @param clientSecret client_secret из настроек
     * @return токен и время жизни
     */
    @PostMapping(value = "/token/")
    AvitoTokenResponse token(@RequestParam("grant_type") String grantType,
                             @RequestParam("client_id") String clientId,
                             @RequestParam("client_secret") String clientSecret
    );

    record AvitoTokenResponse(@JsonProperty("access_token") String accessToken,
                              @JsonProperty("expires_in") long expiresIn) {
    }
}
