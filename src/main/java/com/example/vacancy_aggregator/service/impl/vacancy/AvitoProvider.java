package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.client.avito.AvitoFeign;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.data.vacancy.util.AvitoMapper;
import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse;
import com.example.vacancy_aggregator.location.service.impl.AvitoLocationService;
import com.example.vacancy_aggregator.service.VacancyProvider;
import com.example.vacancy_aggregator.service.VacancyQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvitoProvider implements VacancyProvider {

    private final AvitoFeign feign;
    private final AvitoMapper mapper;
    private final AvitoLocationService avitoLoc;

    @Override
    public String providerName() {
        return "avito";
    }

    @Override
    @RateLimiter(name = "avito")
    public List<Vacancy> search(VacancyQuery query) {

        int region = avitoLoc.findRegionId(query.area())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Неизвестный для Авито регион: " + query.area()));

        AvitoSearchResponse resp = feign.search(
                query.text(),
                query.page(),
                query.perPage(),
                region);

        return resp.vacancies().stream()
                .map(mapper::toVacancy)
                .toList();
    }
}