package com.example.vacancy_aggregator.config.hh;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class HhFeignConfig {

    @Value("${hh.api.base-url}")
    private String baseUrl;

    @Bean
    public RateLimiterConfig hhRateConfig(@Value("${hh.api.rate-limit-per-sec}") int limit) {
        return RateLimiterConfig.custom()
                .limitForPeriod(limit)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(500))
                .build();
    }

//    @Bean
//    public HhFeign hhFeignClient(RateLimiterConfig config) {
//        RateLimiter limiter = RateLimiter.of("hh", config);
//
//        Supplier<HhFeign> supplier = () -> Feign.builder()
//                .client(new ApacheHttpClient())
//                .decoder(new JacksonDecoder())
//                .target(HhFeign.class, baseUrl);
//
//        Supplier<HhFeign> decorated = RateLimiter.decorateSupplier(limiter, supplier);
//        return decorated.get();
//    }
}
