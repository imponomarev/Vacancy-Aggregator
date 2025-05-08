package com.example.vacancy_aggregator.auth.web;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.auth.service.YooKassaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;



/**
 * Создаёт счёт в YooKassa на 299 ₽ и отдаёт фронту URL для оплаты.
 */
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final UserRepository    users;
    private final YooKassaService kassaService;

    @PostMapping("/pro")
    public String buyPro(@AuthenticationPrincipal UserDetails userDetails) {

        User user = users.findByEmail(userDetails.getUsername())
                .orElseThrow();
        return kassaService.createPayment(user);
    }
}
