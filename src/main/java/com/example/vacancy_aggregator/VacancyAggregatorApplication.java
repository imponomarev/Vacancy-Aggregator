package com.example.vacancy_aggregator;

import feign.config.avito.AvitoProps;
import feign.config.sj.SjProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(
        basePackages = {
                "com.example.vacancy_aggregator.client",
                "com.example.vacancy_aggregator.auth.*",
        }
)
@EnableScheduling
@EnableConfigurationProperties({SjProps.class, AvitoProps.class})
public class VacancyAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VacancyAggregatorApplication.class, args);
    }



}
