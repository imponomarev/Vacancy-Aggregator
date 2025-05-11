package feign.config.avito;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства для Avito API
 * baseUrl – базовый URL сервиса
 * clientId – OAuth2 client_id
 * clientSecret – OAuth2 client_secret
 * rateLimitPerSec – лимит запросов в секунду
 */
@Data
@ConfigurationProperties("avito.api")
public class AvitoProps {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private int rateLimitPerSec;
}
