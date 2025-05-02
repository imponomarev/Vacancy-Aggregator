package com.example.vacancy_aggregator.data.vacancy;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Vacancy {
    private String source;       // "hh"
    private String externalId;   // 7760476
    private String title;
    private String company;
    private String companyUrl;
    private String city;
    private Integer salaryFrom;
    private Integer salaryTo;
    private String currency;
    private String description;
    private String experienceReq;
    private String employmentType;
    private String schedule;
    private OffsetDateTime publishedAt;
    private String url;
}