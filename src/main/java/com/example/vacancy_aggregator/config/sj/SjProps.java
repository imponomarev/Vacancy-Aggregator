package com.example.vacancy_aggregator.config.sj;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sj.api")
@Data
public class SjProps {

    private String clientId;

    private String clientSecret;

    private String baseUrl;

    private int rateLimitPerMin;

}
