package feign.config.sj;

import feign.RequestInterceptor;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * Конфигурация Feign для SuperJob Resume:
 * Добавляет заголовки X-Api-App-Id и Authorization (Bearer token)
 * Настраивает RateLimiter по значению из настроек
 */
public class SjResumeFeignConfig {

    @Value("${sj.api.app-id}")
    private String appId;

    @Value("${sj.api.token}")
    private String token;

    /**
     * Интерцептор для добавления заголовков авторизации.
     */
    @Bean
    public RequestInterceptor sjHeadersInterceptor() {
        return request -> {
            request.header("X-Api-App-Id", appId);
            String bearer = token.startsWith("Bearer ")
                    ? token
                    : "Bearer " + token;
            request.header("Authorization", bearer);
        };
    }

    /**
     * Конфиг Resilience4j RateLimiter.
     */
    @Bean
    public RateLimiterConfig sjRateLimiter(@Value("${sj.api.rate-limit-per-min}") int limit) {
        return RateLimiterConfig.custom()
                .limitForPeriod(limit)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(200))
                .build();
    }
}
