package com.example.vacancy_aggregator.client.yookassa;

import feign.config.yookassa.YooKassaConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.example.vacancy_aggregator.dto.yookassa.YkDto.*;

/**
 * Feign-клиент для работы с REST-API YooKassa v3.
 */
@FeignClient(name = "yk",
        url = "https://api.yookassa.ru/v3",
        configuration = YooKassaConfig.class)
public interface YooKassaFeign {

    /**
     * Создание платежа.
     *
     * @param body запрос {@link CreatePaymentRq}
     * @return ответ {@link CreatePaymentRs}
     */
    @PostMapping("/payments")
    CreatePaymentRs createPayment(@RequestBody CreatePaymentRq body);
}
