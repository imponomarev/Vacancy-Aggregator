package com.example.vacancy_aggregator;

import org.springframework.boot.SpringApplication;

public class TestVacancyAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.from(VacancyAggregatorApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
