package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.data.Resume;
import java.util.List;

public interface ResumeProvider {
    String providerName();
    List<Resume> search(ResumeQuery query);
}
