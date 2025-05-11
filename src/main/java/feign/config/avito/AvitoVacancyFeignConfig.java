package feign.config.avito;

import com.example.vacancy_aggregator.auth.avito.vacancy.AvitoFeignOAuth2Interceptor;
import feign.RequestInterceptor;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.time.Duration;

/**
 * Конфигурация Feign-клиента для Авито:
 * Добавляет OAuth2-Interceptor для авторизации через AvitoFeignOAuth2Interceptor.
 * Настраивает RateLimiterConfig с параметрами из {@link AvitoProps}.
 */
@RequiredArgsConstructor
public class AvitoVacancyFeignConfig {

    private final AvitoProps props;
    private final OAuth2AuthorizedClientManager authClientManager;

    /**
     * Интерцептор, вставляющий в запрос заголовок
     * Authorization: Bearer token
     */
    @Bean
    public RequestInterceptor avitoOAuth2Interceptor() {
        return new AvitoFeignOAuth2Interceptor(authClientManager);
    }

    /**
     * RateLimiterConfig для ограничения количества запросов
     * в соответствии с rateLimitPerSec из AvitoProps.
     */
    @Bean
    public RateLimiterConfig avitoRate() {
        return RateLimiterConfig.custom()
                .limitForPeriod(props.getRateLimitPerSec())
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(300))
                .build();
    }
}
