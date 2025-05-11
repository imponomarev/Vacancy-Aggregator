package feign.config.sj;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства для SuperJob API.
 * Загружаются из префикса "sj.api" в application.yml:
 * clientId, clientSecret, baseUrl и rateLimitPerMin.
 */
@ConfigurationProperties("sj.api")
@Data
public class SjProps {

    /**
     * ID клиента (X-Api-App-Id)
     */
    private String clientId;

    /**
     * Секрет клиента (используется как App-Id для Feign)
     */
    private String clientSecret;

    /**
     * Базовый URL SuperJob API
     */
    private String baseUrl;

    /**
     * Лимит запросов в минуту
     */
    private int rateLimitPerMin;

}
