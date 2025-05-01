package com.example.vacancy_aggregator.config.avito;

import com.example.vacancy_aggregator.auth.avito.vacancy.AvitoFeignOAuth2Interceptor;
import feign.RequestInterceptor;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AvitoVacancyFeignConfig {

    private final AvitoProps props;
    private final OAuth2AuthorizedClientManager authClientManager;

    @Bean
    public RequestInterceptor avitoOAuth2Interceptor() {
        return new AvitoFeignOAuth2Interceptor(authClientManager);
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
