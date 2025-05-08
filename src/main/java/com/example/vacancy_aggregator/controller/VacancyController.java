package com.example.vacancy_aggregator.controller;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.service.util.ExperienceLevel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.vacancy_aggregator.service.VacancyQuery;
import com.example.vacancy_aggregator.service.impl.vacancy.VacancySearchService;

import java.util.List;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancySearchService service;

    public VacancyController(VacancySearchService service) {
        this.service = service;
    }

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
