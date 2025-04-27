package com.example.vacancy_aggregator.client.yookassa;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.example.vacancy_aggregator.dto.yookassa.YkDto.*;

/**
 * Общается с REST-API YooKassa (v3).
 * Базовый URL указан явно, а авторизация - через RequestInterceptor в конфиге.
 */
@FeignClient(name = "yk",
        url = "https://api.yookassa.ru/v3",
        configuration = com.example.vacancy_aggregator.config.yookassa.YooKassaConfig.class)
public interface YooKassaFeign {

    /** POST /payments – создание платежа */
    @PostMapping("/payments")
    CreatePaymentRs createPayment(@RequestBody CreatePaymentRq body);
}
