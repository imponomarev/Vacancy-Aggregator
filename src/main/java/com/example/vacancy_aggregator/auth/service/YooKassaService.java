package com.example.vacancy_aggregator.auth.service;

import com.example.vacancy_aggregator.auth.entity.Payment;
import com.example.vacancy_aggregator.auth.repository.PaymentRepository;
import com.example.vacancy_aggregator.client.yookassa.YooKassaFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

import static com.example.vacancy_aggregator.dto.yookassa.YkDto.*;

/**
 * Тонкая обёртка над Feign-клиентом YooKassa.
 * Отвечает за создание платежа и пост-обработку статусов.
 */
@Service
@RequiredArgsConstructor
public class YooKassaService {

    private final YooKassaFeign feign;
    private final PaymentRepository  payments;

    @Value("${yookassa.return-url}")
    private String returnUrl;

    /** Создаёт платёж и возвращает URL для редиректа пользователя. */
    public String createPayment(Payment p) {

        CreatePaymentRq rq = new CreatePaymentRq(
                // сумма в рублях (копейки -> рубли)
                new Amount("RUB", BigDecimal.valueOf(p.getAmount()).movePointLeft(2)),
                new Confirmation("redirect", returnUrl),
                true,
                Map.of("payment_id", p.getId().toString()),
                "PRO-доступ на месяц"
        );

        CreatePaymentRs rs = feign.createPayment(rq);

        // сохраняем ID платёжки, полученный от YooKassa
        p.setYkPaymentId(rs.id());
        payments.save(p);

        return rs.confirmation().confirmationUrl();
    }

    /** Помечаем платёж как успешный и повышаем роль пользователя. */
    public void markSucceeded(String ykPaymentId) {
        payments.findByYkPaymentId(ykPaymentId).ifPresent(pay -> {
            pay.setStatus(Payment.Status.PAID);
            pay.setPaidAt(OffsetDateTime.now());
            pay.getUser().setRole(com.example.vacancy_aggregator.auth.entity.User.Role.PRO);
            payments.save(pay);
        });
    }
}
