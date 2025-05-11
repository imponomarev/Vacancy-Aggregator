package com.example.vacancy_aggregator.controller;

import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.service.ResumeQuery;
import com.example.vacancy_aggregator.service.impl.resume.ResumeSearchService;
import com.example.vacancy_aggregator.service.util.ResumeEducation;
import com.example.vacancy_aggregator.service.util.ResumeExperience;
import com.example.vacancy_aggregator.service.util.ResumeSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для поиска резюме.
 * Доступен только для пользователей с ролью PRO.
 */
@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeSearchService service;

    /**
     * Выполняет поиск резюме по заданному запросу.
     *
     * @param text        ключевые слова для поиска
     * @param area        регион (название или ID)
     * @param page        номер страницы
     * @param perPage     количество результатов на страницу
     * @param providers   список провайдеров ("hh", "sj", "avito"), необязательный
     * @param salaryFrom  минимальная ожидаемая зарплата, необязательный
     * @param salaryTo    максимальная ожидаемая зарплата, необязательный
     * @param ageFrom     минимальный возраст, необязательный
     * @param ageTo       максимальный возраст, необязательный
     * @param experience  уровень опыта (enum ResumeExperience), необязательный
     * @param schedule    тип занятости (enum ResumeSchedule), необязательный
     * @param education   уровень образования (enum ResumeEducation), необязательный
     * @return список DTO {@link Resume}
     */
    @GetMapping
    public List<Resume> search(@RequestParam String text,
                               @RequestParam String area,
                               @RequestParam Integer page,
                               @RequestParam Integer perPage,
                               @RequestParam(required = false) List<String> providers,
                               @RequestParam(required = false, name = "salaryFrom") Integer salaryFrom,
                               @RequestParam(required = false, name = "salaryTo") Integer salaryTo,
                               @RequestParam(required = false, name = "ageFrom") Integer ageFrom,
                               @RequestParam(required = false, name = "ageTo") Integer ageTo,
                               @RequestParam(required = false) ResumeExperience experience,
                               @RequestParam(required = false) ResumeSchedule schedule,
                               @RequestParam(required = false) ResumeEducation education) {

        ResumeQuery q = new ResumeQuery(text, page, perPage, area, providers,
                salaryFrom, salaryTo, ageFrom, ageTo,
                experience, schedule, education);

        return service.search(q);

    }
}
