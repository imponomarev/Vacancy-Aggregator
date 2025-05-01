package com.example.vacancy_aggregator.config.hh;

import feign.Client;
import feign.okhttp.OkHttpClient;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.Duration;

@Configuration
public class HhResumeFeignConfig {

    @Bean
    public Client feignClient() {
        return new OkHttpClient();
    }

    @Bean
    public RateLimiterConfig hhResumeRateConfig(
            @Value("${hh.api.rate-limit-per-sec}") int limit) {
        return RateLimiterConfig.custom()
                .limitForPeriod(limit)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(500))
                .build();
    }
}
