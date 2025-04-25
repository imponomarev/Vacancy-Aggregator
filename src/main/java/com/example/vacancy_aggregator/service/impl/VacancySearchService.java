package com.example.vacancy_aggregator.service.impl;

import com.example.vacancy_aggregator.data.Vacancy;
import com.example.vacancy_aggregator.service.VacancyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.vacancy_aggregator.service.VacancyQuery;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancySearchService {

    private final List<VacancyProvider> providers;

    public List<Vacancy> search(VacancyQuery query) {
        List<VacancyProvider> toUse = Optional.ofNullable(query.providers())
                .filter(lst -> !lst.isEmpty())
                .map(lst -> providers.stream()
                        .filter(p -> lst.contains(p.providerName()))
                        .toList()
                ).orElse(providers);

        return toUse.stream()
                .flatMap(p -> p.search(query).stream())
                .toList();
    }
}

// return providers.parallelStream()
//                .flatMap(p -> p.search(q).stream())
//        .sorted(Comparator.comparing(Vacancy::getPublishedAt).reversed())
//        .toList();