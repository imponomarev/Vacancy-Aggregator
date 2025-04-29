package com.example.vacancy_aggregator.auth.sj;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="sjAuth", url="${sj.api.base-url}")
public interface SjAuthFeign {
    @PostMapping("/2.0/oauth2/password/")
    TokenResponse token(@RequestParam("login") String email,
                        @RequestParam("password") String pwd,
                        @RequestParam("client_id") String clientId,
                        @RequestParam("client_secret") String secret,
                        @RequestParam("hr") int hr);
}

