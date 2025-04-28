package com.example.vacancy_aggregator.config.avito;

import com.example.vacancy_aggregator.auth.avito.AvitoTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AvitoFeignConfig {

    private final AvitoTokenService ts;
    private final AvitoProps        props;

    @Bean
    public RequestInterceptor bearer() {
        return (RequestTemplate tpl) ->
                tpl.header("Authorization", "Bearer " + ts.token());
    }

    @Bean
    public RateLimiterConfig avitoRate() {
        return RateLimiterConfig.custom()
                .limitForPeriod(props.getRateLimitPerSec())
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(300))
                .build();
    }
}
