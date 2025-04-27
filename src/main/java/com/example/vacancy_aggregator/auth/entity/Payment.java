package com.example.vacancy_aggregator.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "payments")
@Getter @Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // связь с пользователем
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "yk_payment_id", unique = true)
    private String ykPaymentId;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    public enum Status { NEW, PAID, CANCELED }
}
