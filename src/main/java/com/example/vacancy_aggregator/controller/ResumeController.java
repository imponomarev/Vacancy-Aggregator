package com.example.vacancy_aggregator.controller;

import com.example.vacancy_aggregator.data.Resume;
import com.example.vacancy_aggregator.service.ResumeQuery;
import com.example.vacancy_aggregator.service.impl.resume.ResumeSearchService;
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
                               @RequestParam(required = false) List<String> providers) {

        ResumeQuery q = new ResumeQuery(text, page, perPage, area, providers);

        return service.search(q);

    }
}
