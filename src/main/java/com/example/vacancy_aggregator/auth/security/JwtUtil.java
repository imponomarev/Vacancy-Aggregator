package com.example.vacancy_aggregator.auth.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

/**
 * Компонент для генерации и валидации JWT-токенов.
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserDetailsService uds;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long ttl;

    private Key key;

    /**
     * Инициализация HMAC-ключа после чтения свойств.
     */
    @PostConstruct
    void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Генерирует JWT для заданного username.
     *
     * @param username имя пользователя (email)
     * @return скомпонованная JWT строка
     */
    public String generate(String username) {

        var user = uds.loadUserByUsername(username);
        String role = user.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("FREE");

        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ttl))) // 24h
                .signWith(key)
                .compact();
    }

    /**
     * Извлекает subject (username) из JWT.
     *
     * @param token JWT строка
     * @return username (email)
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}

