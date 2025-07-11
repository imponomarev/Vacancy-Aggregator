package com.example.vacancy_aggregator.auth.service;

import com.example.vacancy_aggregator.auth.entity.Payment;
import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.auth.repository.PaymentRepository;
import com.example.vacancy_aggregator.client.yookassa.YooKassaFeign;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

import static com.example.vacancy_aggregator.dto.yookassa.YkDto.*;

/**
 * Сервис-обёртка над Feign-клиентом YooKassa.
 * Отвечает за создание платежа и пост-обработку статусов.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class YooKassaService {

    private final YooKassaFeign feign;
    private final PaymentRepository payments;

    /**
     * URL для возврата пользователя после оплаты (redirect).
     */
    @Value("${yookassa.return-url}")
    private String returnUrl;

    /**
     * Создаёт новый платёж на фиксированную сумму 299 ₽.
     *
     * @param user текущий {@link User}, приобретающий PRO-доступ
     * @return URL для перенаправления на оплату
     */
    public String createPayment(User user) {
        try {
            CreatePaymentRq rq = new CreatePaymentRq(
                    // сумма в рублях (копейки -> рубли)
                    new Amount("RUB", BigDecimal.valueOf(299_00).movePointLeft(2)),
                    new Confirmation("redirect", returnUrl),
                    true,
                    Map.of(),
                    "PRO-доступ на месяц"
            );

            CreatePaymentRs rs = feign.createPayment(rq);

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setAmount(299_00);
            payment.setStatus(Payment.Status.NEW);
            payment.setYkPaymentId(rs.id());
            payments.save(payment);

            // 3) Возвращаем URL для редиректа или токен для виджета
            return rs.confirmation().confirmationUrl();
        } catch (FeignException.Unauthorized e) {
            log.warn("YooKassa unauthorized, returning dummy token: {}", e.getMessage());
            return "invalid-token";
        }
    }

    /**
     * Помечает платёж успешным и повышает роль пользователя до PRO.
     *
     * @param ykPaymentId идентификатор платежа в YooKassa
     */
    public void markSucceeded(String ykPaymentId) {
        payments.findByYkPaymentId(ykPaymentId).ifPresent(pay -> {
            pay.setStatus(Payment.Status.PAID);
            pay.setPaidAt(OffsetDateTime.now());
            pay.getUser().setRole(com.example.vacancy_aggregator.auth.entity.User.Role.PRO);
            payments.save(pay);
        });
    }
}
