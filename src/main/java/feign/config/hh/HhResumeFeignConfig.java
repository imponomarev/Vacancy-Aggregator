package feign.config.hh;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Конфигурация Feign для hh.ru Resume:
 * подставляет Authorization: Bearer {app-token}.
 */
public class HhResumeFeignConfig {

    @Value("${hh.api.app-token}")
    private String token;

    @Bean
    public RequestInterceptor sjHeadersInterceptor() {
        return request -> {
            String bearer = token.startsWith("Bearer ")
                    ? token
                    : "Bearer " + token;
            request.header("Authorization", bearer);
        };
    }
}

