package feign.config.avito;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("avito.api")
public class AvitoProps {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private int    rateLimitPerSec;
}
