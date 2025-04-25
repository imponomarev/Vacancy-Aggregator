package com.example.vacancy_aggregator.auth.sj;

record TokenResponse(
        String access_token,
        String refresh_token,
        long ttl) {
}
