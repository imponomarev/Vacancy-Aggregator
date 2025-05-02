package com.example.vacancy_aggregator.auth.sj;

import feign.config.sj.SjProps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SjTokenService {

    private final SjAuthFeign auth;
    private final SjProps props;
    private volatile Token ttlToken;

    /**
     * Возвращает true, если токен уже был получен и ещё не истёк.
     */
    public boolean isInitialized() {
        return ttlToken != null && ttlToken.expiresAt > System.currentTimeMillis();
    }

    public String bearer() {
        // при первом вызове или если токен просрочен — обновляем
        if (!isInitialized()) {
            refresh();
        }
        return "Bearer " + ttlToken.value;
    }

    private synchronized void refresh() {
        TokenResponse tr = auth.token("api@dummy", "dummy123",
                props.getClientId(), props.getClientSecret(), 0);

        ttlToken = new Token(tr.access_token(),
                System.currentTimeMillis() + tr.ttl() * 1000);
    }

    private record Token(String value, long expiresAt) {
    }
}
