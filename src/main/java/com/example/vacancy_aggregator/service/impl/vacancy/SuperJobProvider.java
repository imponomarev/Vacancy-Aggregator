package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.client.sj.SjFeign;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.data.vacancy.util.SjMapper;
import com.example.vacancy_aggregator.dto.sj.SjSearchResponse;
import com.example.vacancy_aggregator.location.data.Location;
import com.example.vacancy_aggregator.location.service.impl.LocationDirectory;
import com.example.vacancy_aggregator.service.VacancyProvider;
import com.example.vacancy_aggregator.service.VacancyQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

        Integer experience = query.experience() == null ? null : query.experience().toSj();

        log.debug("[SJ] search text='{}' town={} pay={}..{} exp={}",
                query.text(), sjTown, query.salaryFrom(), query.salaryTo(), experience);

        SjSearchResponse resp = client.search(
                query.text(),
                sjTown,
                query.page(),
                query.perPage(),
                query.salaryFrom(),
                query.salaryTo(),
                experience
        );
        return resp.objects().stream()
                .map(mapper::toVacancy)
                .toList();
    }
}
