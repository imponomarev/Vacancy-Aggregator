package com.example.vacancy_aggregator.auth.security;

import com.example.vacancy_aggregator.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret}") String secret;
    private final long TTL = Duration.ofHours(24).toMillis();

    public String generate(User u){
        var now = Instant.now();
        return Jwts.builder()
                .subject(u.getId().toString())
                .claim("role", u.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(TTL)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
    public Jws<Claims> parse(String token){
        return Jwts.parser().setSigningKey(secret.getBytes()).build().parseSignedClaims(token);
    }
}

