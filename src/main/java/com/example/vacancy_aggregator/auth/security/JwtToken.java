package com.example.vacancy_aggregator.auth.security;

/**
 * DTO для передачи JWT в ответе на /auth/login.
 *
 * @param token строка JWT
 */
public record JwtToken(String token) {
}
