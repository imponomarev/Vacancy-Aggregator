package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.dto.hh.HhSearchResponse;
import com.example.vacancy_aggregator.client.hh.HhFeign;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.data.vacancy.util.HhMapper;
import com.example.vacancy_aggregator.location.service.impl.LocationDirectory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.vacancy_aggregator.service.VacancyProvider;
import com.example.vacancy_aggregator.service.VacancyQuery;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HhProvider implements VacancyProvider {

    private final HhFeign client;
    private final HhMapper mapper;
    private final LocationDirectory locDir;

//    public HhProvider(HhFeign client, HhMapper mapper, LocationDirectory locDir) {
//        this.client = client;
//        this.mapper = mapper;
//        this.locDir = locDir;
//    }

    @Override
    public String providerName() {
        return "hh";
    }

    @Override
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "hh")
    public List<Vacancy> search(VacancyQuery query) {

        String rawArea = query.area();
        String hhArea = locDir.resolve(rawArea)
                .flatMap(loc -> Optional.ofNullable(loc.hhId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Не удалось определить HH-region-id для: " + rawArea)
                );

        Integer salary = query.salaryFrom();
        String experience = query.experience() == null ? null : query.experience().toHh();

        HhSearchResponse response = client.search(
                query.text(),
                hhArea,
                query.page(),
                query.perPage(),
                salary,
                experience);

        return response.items().stream()
                .map(mapper::toVacancy)
                .toList();
    }
}
