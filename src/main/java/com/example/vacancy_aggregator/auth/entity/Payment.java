package com.example.vacancy_aggregator.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Сущность Платёж за PRO-доступ.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, создавший платёж
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Идентификатор платежа в YooKassa (v3)
     */
    @Column(name = "yk_payment_id", unique = true)
    private String ykPaymentId;

    /**
     * Сумма в копейках
     */
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    /**
     * Возможные статусы платежа
     */
    public enum Status {NEW, PAID, CANCELED}
}
