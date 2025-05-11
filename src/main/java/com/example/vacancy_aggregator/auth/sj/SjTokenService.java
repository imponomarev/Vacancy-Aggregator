package com.example.vacancy_aggregator.auth.sj;

import feign.config.sj.SjProps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Кэширует и обновляет токен SuperJob для resume-API (password grant).
 */
@Service
@RequiredArgsConstructor
public class SjTokenService {

    private final SjAuthFeign auth;
    private final SjProps props;
    private volatile Token ttlToken;

    /**
     * Проверяет, что токен инициализирован и ещё валиден.
     *
     * @return true, если можно переиспользовать
     */
    public boolean isInitialized() {
        return ttlToken != null && ttlToken.expiresAt > System.currentTimeMillis();
    }

    /**
     * Возвращает строку "Bearer <token>", обновляя токен при необходимости.
     */
    public String bearer() {
        // при первом вызове или если токен просрочен — обновляем
        if (!isInitialized()) {
            refresh();
        }
        return "Bearer " + ttlToken.value;
    }

    /**
     * Синхронно запрашивает новый токен и обновляет кеш.
     */
    private synchronized void refresh() {
        TokenResponse tr = auth.token("api@dummy", "dummy123",
                props.getClientId(), props.getClientSecret(), 0);

        ttlToken = new Token(tr.access_token(),
                System.currentTimeMillis() + tr.ttl() * 1000);
    }

    /**
     * Внутренный record для хранения токена и времени жизни.
     */
    private record Token(String value, long expiresAt) {
    }
}
