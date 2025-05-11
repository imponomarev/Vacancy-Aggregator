package com.example.vacancy_aggregator.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность пользователя
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальный email (используется как логин)
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Хэш пароля
     */
    @Column(name = "pwd_hash", nullable = false)
    private String pwdHash;

    /**
     * Роль пользователя в системе
     */
    @Enumerated(EnumType.STRING)
    private Role role = Role.FREE;

    /**
     * Возможные роли
     */
    public enum Role {FREE, PRO}
}
