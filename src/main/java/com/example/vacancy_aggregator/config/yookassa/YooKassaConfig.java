package com.example.vacancy_aggregator.config.yookassa;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Настройки, общие для всех вызовов YooKassa.
 */
@Configuration
public class YooKassaConfig {

    /** Basic-заголовок формируем один раз и подставляем во все запросы */
    @Bean
    public RequestInterceptor auth(@Value("${yookassa.shop-id}") String shopId,
                                   @Value("${yookassa.secret-key}") String secret) {

        String pair = shopId + ":" + secret;
        String basic = "Basic " +
                Base64.getEncoder().encodeToString(pair.getBytes(StandardCharsets.UTF_8));

        return template -> template.header("Authorization", basic);
    }

    /** Читаем тело 4xx/5xx и кидаем FeignException с сообщением YooKassa */
    @Bean
    public ErrorDecoder yooErrorDecoder() {
        return new ErrorDecoder.Default();
    }
}