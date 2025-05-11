package com.example.vacancy_aggregator.controller;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.service.util.ExperienceLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.vacancy_aggregator.service.VacancyQuery;
import com.example.vacancy_aggregator.service.impl.vacancy.VacancySearchService;

import java.util.List;

/**
 * REST-контроллер для поиска вакансий.
 * Поддерживает фильтрацию по ключевому слову, региону, пагинацию,
 * выбор провайдеров и дополнительные параметры (минимальная/максимальная зарплата, опыт).
 */
@RestController
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancySearchService service;

    /**
     * Поиск вакансий по заданным критериям.
     *
     * @param text       ключевые слова для поиска вакансий
     * @param area       человекочитаемое название региона
     * @param page       номер страницы
     * @param perPage    количество элементов на страницу
     * @param providers  (необязательный) список провайдеров; если не задан, ищутся все доступные
     * @param salaryFrom (необязательный) минимальная зарплата
     * @param salaryTo   (необязательный) максимальная зарплата
     * @param experience (необязательный) требуемый уровень опыта
     * @return список найденных вакансий, представленных в виде DTO {@link Vacancy}
     */
    @GetMapping
    public List<Vacancy> search(@RequestParam String text,
                                @RequestParam String area,
                                @RequestParam Integer page,
                                @RequestParam Integer perPage,
                                @RequestParam(required = false) List<String> providers,
                                @RequestParam(required = false) Integer salaryFrom,
                                @RequestParam(required = false) Integer salaryTo,
                                @RequestParam(required = false) ExperienceLevel experience) {
        VacancyQuery query = new VacancyQuery(text, page, perPage, area, providers, salaryFrom, salaryTo, experience);
        return service.search(query);
    }

}
