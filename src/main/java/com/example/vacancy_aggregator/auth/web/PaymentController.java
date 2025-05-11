package com.example.vacancy_aggregator.auth.web;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.auth.service.YooKassaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер для создания платежей через YooKassa.
 * Предназначен для генерации счёта на покупку PRO-доступа (299 ₽) и
 * передачи фронтенду URL для перенаправления пользователя на оплату.
 */
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final UserRepository users;
    private final YooKassaService kassaService;

    /**
     * Инициализирует платёж на PRO-доступ для текущего пользователя.
     *
     * @param userDetails данные аутентифицированного пользователя
     * @return URL для редиректа пользователя на страницу оплаты в YooKassa
     */
    @PostMapping("/pro")
    public String buyPro(@AuthenticationPrincipal UserDetails userDetails) {

        User user = users.findByEmail(userDetails.getUsername())
                .orElseThrow();
        return kassaService.createPayment(user);
    }
}
