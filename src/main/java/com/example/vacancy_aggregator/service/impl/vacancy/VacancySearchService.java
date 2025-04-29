package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.data.Vacancy;
import com.example.vacancy_aggregator.service.VacancyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.vacancy_aggregator.service.VacancyQuery;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancySearchService {

    private final List<VacancyProvider> providers;

    public List<Vacancy> search(VacancyQuery query) {

        boolean isPro = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PRO"));

        return providers.stream()
                .filter(p -> isPro || !"avito".equals(p.providerName()))
                .flatMap(p -> p.search(query).stream())
                .toList();
    }
}
