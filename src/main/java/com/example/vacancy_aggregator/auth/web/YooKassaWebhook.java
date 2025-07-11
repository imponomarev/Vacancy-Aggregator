package com.example.vacancy_aggregator.auth.web;

import com.example.vacancy_aggregator.auth.service.YooKassaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

import static com.example.vacancy_aggregator.dto.yookassa.YkDto.*;

/**
 * Контроллер для приёма Webhook-уведомлений от YooKassa.
 * Процедура обработки:
 * Верификация HMAC-подписи из заголовка Content-Hmac-Sha256;
 * Десериализация JSON в {@link PaymentSucceededNotification};
 */
@RestController
@RequestMapping("/yookassa/webhook")
@RequiredArgsConstructor
public class YooKassaWebhook {

    private final YooKassaService service;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Секрет для HMAC, настраивается в кабинете YooKassa
     */
    @Value("${yookassa.webhook-secret}")
    String secret;

    /**
     * Принимает POST запрос от YooKassa с телом уведомления.
     *
     * @param sig  заголовок Content-Hmac-Sha256 — HMAC-подпись тела
     * @param body необработанное тело JSON
     */
    @PostMapping
    public ResponseEntity<String> handle(@RequestHeader("Content-Hmac-Sha256") String sig,
                                         @RequestBody String body) {

        // 1. проверяем подпись
        String calc = Hex.encodeHexString(
                HmacUtils.hmacSha256(secret.getBytes(StandardCharsets.UTF_8),
                        body.getBytes(StandardCharsets.UTF_8)));

        if (!calc.equals(sig)) {
            return ResponseEntity.status(400).body("bad signature");
        }

        // 2. десериализуем и обрабатываем статус
        try {
            PaymentSucceededNotification n =
                    mapper.readValue(body, PaymentSucceededNotification.class);

            if ("succeeded".equals(n.object().status()))
                service.markSucceeded(n.object().id());

        } catch (Exception e) {
            return ResponseEntity.status(400).body("bad json");
        }

        return ResponseEntity.ok("ok");
    }
}
