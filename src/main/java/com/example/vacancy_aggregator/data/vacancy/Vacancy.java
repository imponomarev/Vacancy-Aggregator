package com.example.vacancy_aggregator.data.vacancy;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO вакансии, возвращаемое API.
 */
@Data
@Builder
public class Vacancy {
    /**
     * Идентификатор провайдера: "hh", "sj" или "avito"
     */
    private String source;
    /**
     * Внешний ID вакансии в системе провайдера
     */
    private String externalId;
    /**
     * Заголовок вакансии
     */
    private String title;
    /**
     * Название компании
     */
    private String company;
    /**
     * Ссылка на страницу компании
     */
    private String companyUrl;
    /**
     * Город размещения вакансии
     */
    private String city;
    /**
     * Минимальная зарплата
     */
    private Integer salaryFrom;
    /**
     * Максимальная зарплата
     */
    private Integer salaryTo;
    /**
     * Валюта зарплаты
     */
    private String currency;
    /**
     * Описание обязанностей
     */
    private String description;
    /**
     * Требуемый опыт работы
     */
    private String experienceReq;
    /**
     * Тип занятости
     */
    private String employmentType;
    /**
     * График работы (человеческое название)
     */
    private String schedule;
    /**
     * Дата и время публикации вакансии
     */
    private OffsetDateTime publishedAt;
    /**
     * URL на страницу вакансии
     */
    private String url;
}