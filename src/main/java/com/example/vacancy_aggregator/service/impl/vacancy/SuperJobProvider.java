package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.client.sj.SjFeign;
import com.example.vacancy_aggregator.data.Vacancy;
import com.example.vacancy_aggregator.data.util.SjMapper;
import com.example.vacancy_aggregator.dto.sj.SjSearchResponse;
import com.example.vacancy_aggregator.location.data.Location;
import com.example.vacancy_aggregator.location.service.impl.LocationDirectory;
import com.example.vacancy_aggregator.service.VacancyProvider;
import com.example.vacancy_aggregator.service.VacancyQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperJobProvider implements VacancyProvider {

    private final SjFeign client;
    private final SjMapper mapper;
    private final LocationDirectory locDir;

    @Override
    public String providerName() {
        return "sj";
    }

    @Override
    @RateLimiter(name = "sj")
    public List<Vacancy> search(VacancyQuery query) {

        String sjTown = locDir.resolve(query.area())
                .map(Location::sjId)
                .map(Object::toString)
                .orElse(query.area());

        SjSearchResponse resp = client.search(
                query.text(),
                sjTown,
                query.page(),
                query.perPage()
        );
        return resp.objects().stream()
                .map(mapper::toVacancy)
                .toList();
    }

    @Override
    public Vacancy getById(String externalId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


}
