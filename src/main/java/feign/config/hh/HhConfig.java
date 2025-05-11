package feign.config.hh;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Общая конфигурация для HH Feign-клиентов.
 * Настраивает Resilience4j RateLimiter под HH API:
 * лимит запросов в секунду, таймаут ожидания.
 */
@Configuration
public class HhConfig {

    /**
     * Базовый URL hh.ru
     */
    @Value("${hh.api.base-url}")
    private String baseUrl;

    /**
     * Создает RateLimiterConfig для HH:
     * limitForPeriod = hh.api.rate-limit-per-sec
     * refreshPeriod = 1 секунда
     * timeoutDuration = 500 мс
     *
     * @param limit число запросов в секунду
     * @return RateLimiterConfig для использования в @RateLimiter
     */
    @Bean
    public RateLimiterConfig hhRateConfig(@Value("${hh.api.rate-limit-per-sec}") int limit) {
        return RateLimiterConfig.custom()
                .limitForPeriod(limit)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(500))
                .build();
    }

}
