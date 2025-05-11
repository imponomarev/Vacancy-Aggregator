package com.example.vacancy_aggregator.auth.sj;

/**
 * Ответ SuperJob OAuth2 password grant:
 *
 * @param access_token  сам токен
 * @param refresh_token токен для обновления
 * @param ttl           время жизни в секундах
 */
record TokenResponse(
        String access_token,
        String refresh_token,
        long ttl) {
}
