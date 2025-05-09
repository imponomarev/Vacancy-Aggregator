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

@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeSearchService service;

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
