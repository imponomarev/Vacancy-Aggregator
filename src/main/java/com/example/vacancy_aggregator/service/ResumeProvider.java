package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.data.resume.Resume;
import java.util.List;

public interface ResumeProvider {
    String providerName();
    List<Resume> search(ResumeQuery query);
}
