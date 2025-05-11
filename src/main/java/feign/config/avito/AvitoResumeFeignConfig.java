package feign.config.avito;

import com.example.vacancy_aggregator.auth.avito.resume.AvitoTokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * Конфигурация Feign-клиента для Avito Resume:
 * Интерцептор добавляет заголовок Authorization: Bearer token
 * Токен получает из {@link AvitoTokenService}
 */
public class AvitoResumeFeignConfig {
    @Bean
    public RequestInterceptor avitoAuthInterceptor(AvitoTokenService tokenService) {
        return template -> {
            String headerValue = "Bearer " + tokenService.token();
            template.header("Authorization", headerValue);
        };
    }
}
