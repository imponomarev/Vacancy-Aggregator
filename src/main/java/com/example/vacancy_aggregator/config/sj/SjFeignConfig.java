package com.example.vacancy_aggregator.config.sj;

import com.example.vacancy_aggregator.auth.sj.SjTokenService;
import feign.RequestInterceptor;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class SjFeignConfig {

    @Bean
    public RequestInterceptor sjInterceptor(SjProps p, SjTokenService ts) {
        log.info("ID {}, ключ {}", p.getClientId(), p.getClientSecret());
        return req -> {
            req.header("X-Api-App-Id", p.getClientSecret());
        };
    }

    @Bean
    public RateLimiterConfig sjRate(@Value("${sj.api.rate-limit-per-min}") int limit) {
        return RateLimiterConfig.custom()
                .limitForPeriod(limit)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(200))
                .build();
    }
}
