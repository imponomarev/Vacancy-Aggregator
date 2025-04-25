package com.example.vacancy_aggregator.controller;

import com.example.vacancy_aggregator.data.Vacancy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.vacancy_aggregator.service.VacancyQuery;
import com.example.vacancy_aggregator.service.impl.VacancySearchService;

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
                                @RequestParam(required = false) List<String> providers) {
        VacancyQuery query = new VacancyQuery(text, page, perPage, area, providers);
        return service.search(query);
    }

}
