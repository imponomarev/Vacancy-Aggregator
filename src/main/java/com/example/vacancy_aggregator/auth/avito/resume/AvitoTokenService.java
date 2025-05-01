package com.example.vacancy_aggregator.auth.avito.resume;

import com.example.vacancy_aggregator.config.avito.AvitoProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public synchronized String token() {

        if (token != null && Instant.now().isBefore(expiresAt)) {
            return token;
        }

        var resp = authFeign.token("client_credentials", props.getClientId(), props.getClientSecret());
        log.debug("token response from Avito: {}", resp);

        this.token = resp.accessToken();
        this.expiresAt = Instant.now().plusSeconds(resp.expiresIn() - 60);

        return token;
    }
}
