package com.example.vacancy_aggregator.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "pwd_hash", nullable = false)
    private String pwdHash;

    @Enumerated(EnumType.STRING)
    private Role role = Role.FREE;

    public enum Role {FREE, PRO}
}
