package com.example.vacancy_aggregator;

import com.example.vacancy_aggregator.config.sj.SjProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableFeignClients(
        basePackages = {
                "com.example.vacancy_aggregator.client",
                "com.example.vacancy_aggregator.auth.sj",
        }
)
@EnableScheduling
@EnableConfigurationProperties(SjProps.class)
public class VacancyAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VacancyAggregatorApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
