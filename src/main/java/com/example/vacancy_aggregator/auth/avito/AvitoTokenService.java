package com.example.vacancy_aggregator.auth.avito;

import com.example.vacancy_aggregator.config.avito.AvitoProps;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvitoTokenService {

    private final AvitoProps props;
    private final AvitoAuthFeign authFeign;

    private volatile String token;
    private volatile Instant expiresAt = Instant.EPOCH;

    @PostConstruct
    public void init() {
        refresh();
    }

    /**
     * каждые 25 минут – Avito выдаёт токен на 30 мин.
     */
    @Scheduled(fixedDelay = 1_500_000)
    public void refresh() {
        try {
            var rsp = authFeign.token(
                    "client_credentials",
                    props.getClientId(),
                    props.getClientSecret()
            );
            token = rsp.accessToken();
            expiresAt = Instant.now().plusSeconds(rsp.expiresIn() - 30);
            log.info("Avito token refreshed, expires {}", expiresAt);
        } catch (FeignException e) {
            log.error("Cannot refresh Avito token: {}", e.contentUTF8());
        }
    }

    public String token() {
        return token;
    }
}
