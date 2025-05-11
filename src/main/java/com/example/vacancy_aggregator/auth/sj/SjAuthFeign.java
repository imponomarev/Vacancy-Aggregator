package com.example.vacancy_aggregator.auth.sj;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для получения токена по паролю из API SuperJob.
 */
@FeignClient(name="sjAuth", url="${sj.api.base-url}")
public interface SjAuthFeign {

    /**
     * @param email        логин пользователя
     * @param pwd          пароль
     * @param clientId     client_id приложения
     * @param secret       client_secret приложения
     * @param hr           флаг HR (0 или 1)
     * @return токен и время жизни
     */
    @PostMapping("/2.0/oauth2/password/")
    TokenResponse token(@RequestParam("login") String email,
                        @RequestParam("password") String pwd,
                        @RequestParam("client_id") String clientId,
                        @RequestParam("client_secret") String secret,
                        @RequestParam("hr") int hr);
}

