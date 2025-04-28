package com.example.vacancy_aggregator.config.avito;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("avito.api")
public class AvitoProps {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private int    rateLimitPerSec;
}
