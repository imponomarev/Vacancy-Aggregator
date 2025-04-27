package com.example.vacancy_aggregator.auth.repository;

import com.example.vacancy_aggregator.auth.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByYkPaymentId(String paymentId);
}
