package feign.config.sj;

import feign.RequestInterceptor;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

public class SjResumeFeignConfig {

    @Value("${sj.api.app-id}")
    private String appId;

    @Value("${sj.api.token}")
    private String token;

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

    @Bean
    public RateLimiterConfig sjRateLimiter(@Value("${sj.api.rate-limit-per-min}") int limit) {
        return RateLimiterConfig.custom()
                .limitForPeriod(limit)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(200))
                .build();
    }
}
