package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.service.util.ResumeEducation;
import com.example.vacancy_aggregator.service.util.ResumeExperience;
import com.example.vacancy_aggregator.service.util.ResumeSchedule;

import java.util.List;

/**
 * Параметры, которые приходят из HTTP-запроса /resumes:
 *
 * @param text        ключевое слово
 * @param page        номер страницы
 * @param perPage     размер страницы
 * @param area        регион (название или ID)
 * @param providers   список провайдеров для фильтрации
 * @param salaryFrom  минимальная зарплата
 * @param salaryTo    максимальная зарплата
 * @param ageFrom     минимальный возраст
 * @param ageTo       максимальный возраст
 * @param experience  уровень опыта (enum ResumeExperience)
 * @param schedule    тип занятости (enum ResumeSchedule)
 * @param education   уровень образования (enum ResumeEducation)
 */
public record ResumeQuery(
        String text,
        Integer page,
        Integer perPage,
        String area,
        List<String> providers,
        Integer salaryFrom,
        Integer salaryTo,
        Integer ageFrom,
        Integer ageTo,
        ResumeExperience experience,
        ResumeSchedule schedule,
        ResumeEducation education
) {
}