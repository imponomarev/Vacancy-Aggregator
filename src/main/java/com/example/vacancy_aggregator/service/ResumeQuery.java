package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.service.util.ResumeEducation;
import com.example.vacancy_aggregator.service.util.ResumeExperience;
import com.example.vacancy_aggregator.service.util.ResumeSchedule;

import java.util.List;

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