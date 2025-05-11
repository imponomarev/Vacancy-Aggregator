package feign.config.yookassa;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Конфигурация общего поведения всех запросов к YooKassa:
 */
public class YooKassaConfig {

    /**
     * Интерцептор для установки заголовка Authorization: Basic <base64(shopId:secret)>.
     *
     * @param shopId идентификатор магазина
     * @param secret секретный ключ магазина
     * @return RequestInterceptor, добавляющий Basic-Auth
     */
    @Bean
    public RequestInterceptor auth(@Value("${yookassa.shop-id}") String shopId,
                                   @Value("${yookassa.secret-key}") String secret) {

        String pair = shopId + ":" + secret;
        String basic = "Basic " +
                Base64.getEncoder().encodeToString(pair.getBytes(StandardCharsets.UTF_8));

        return template -> template.header("Authorization", basic);
    }

    /**
     * Декодер ошибок, который при 4xx/5xx будет выбрасывать FeignException
     * с телом ответа от YooKassa.
     */
    @Bean
    public ErrorDecoder yooErrorDecoder() {
        return new ErrorDecoder.Default();
    }
}