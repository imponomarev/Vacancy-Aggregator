package com.example.vacancy_aggregator.auth.security;

import org.springframework.context.annotation.Bean;

public class JwtFilter {

    @Bean
    SecurityFilterChain api(HttpSecurity http, JwtUtil jwt) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/auth/**","/webhook/**").permitAll()
                        .requestMatchers("/vacancies/**").hasAnyRole("FREE","PRO")
                        .anyRequest().denyAll())
                .addFilterBefore(new JwtFilter(jwt), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
