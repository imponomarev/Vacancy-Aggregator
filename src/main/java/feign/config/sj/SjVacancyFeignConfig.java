package feign.config.sj;

import feign.RequestInterceptor;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * Конфигурация Feign-клиента SuperJob.
 * — Interceptor добавляет заголовок X-Api-App-Id для аутентификации.
 * — Bean RateLimiterConfig настраивает предел запросов в минуту.
 */
@Slf4j
public class SjVacancyFeignConfig {

    /**
     * Интерцептор, который в каждый запрос SuperJob добавляет:
     * X-Api-App-Id: clientSecret;
     *
     * @param p свойства SJ (clientId, clientSecret)
     * @return Feign RequestInterceptor
     */
    @Bean
    public RequestInterceptor sjInterceptor(SjProps p) {
        log.info("ID {}, ключ {}", p.getClientId(), p.getClientSecret());
        return req -> {
            req.header("X-Api-App-Id", p.getClientSecret());
        };
    }

    /**
     * Конфигурирует Resilience4j RateLimiter:
     * лимит на период = {@code limit} запросов в минуту
     * timeout = 200 мс
     *
     * @param limit число запросов в минуту из application.yml
     * @return RateLimiterConfig
     */
    @Bean
    public RateLimiterConfig sjRate(@Value("${sj.api.rate-limit-per-min}") int limit) {
        return RateLimiterConfig.custom()
                .limitForPeriod(limit)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(200))
                .build();
    }
}
