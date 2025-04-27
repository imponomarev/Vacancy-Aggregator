package com.example.vacancy_aggregator.dto.yookassa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Содержит ВСЕ необходимые модели для createPayment + webhook.
 */
public interface YkDto {

    // ---------- Запрос на создание платежа ----------
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record CreatePaymentRq(
            Amount amount,
            Confirmation confirmation,
            boolean capture,
            Map<String, String> metadata,
            String description
    ) {
    }

    record Amount(String currency, BigDecimal value) {
    }

    record Confirmation(@JsonProperty("type") String type,
                        @JsonProperty("return_url") String returnUrl) {
    }

    // ---------- Ответ YooKassa ----------
    record CreatePaymentRs(String id,
                           String status,
                           ConfirmationRedirect confirmation) {
    }

    record ConfirmationRedirect(
            @JsonProperty("confirmation_url") String confirmationUrl) {
    }

    // ---------- Структура уведомления ----------
    record PaymentSucceededNotification(
            @JsonProperty("object") PaymentObj object) {

        public record PaymentObj(String id,
                                 Amount amount,
                                 String status,
                                 Map<String, String> metadata) {
        }
    }
}
