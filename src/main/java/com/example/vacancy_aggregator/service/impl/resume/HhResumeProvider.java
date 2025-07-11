package com.example.vacancy_aggregator.service.impl.resume;

import com.example.vacancy_aggregator.client.hh.HhResumeFeign;
import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.data.resume.util.HhResumeMapper;
import com.example.vacancy_aggregator.location.service.impl.LocationDirectory;
import com.example.vacancy_aggregator.service.ResumeProvider;
import com.example.vacancy_aggregator.service.ResumeQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация {@link ResumeProvider} для hh.ru.
 * Добавляет HTTP-заголовок Authorization (Bearer app-token).
 * Преобразует enum-значения в параметры API.
 */
@Service
@RequiredArgsConstructor
public class HhResumeProvider implements ResumeProvider {

    private final HhResumeFeign client;
    private final HhResumeMapper mapper;
    private final LocationDirectory locDir;

    @Override
    public String providerName() {
        return "hh";
    }

    @Override
    @RateLimiter(name = "hh")
    public List<Resume> search(ResumeQuery q) {
        String hhArea = locDir.resolve(q.area())
                .flatMap(l -> l.hhId().describeConstable())
                .orElse(q.area());

        var resp = client.search(q.text(), hhArea, q.page(), q.perPage(),
                q.salaryFrom(), q.salaryTo(),
                q.ageFrom(), q.ageTo(),
                q.experience() == null ? null : q.experience().hhId,
                q.schedule() == null ? null : q.schedule().hhId,
                q.education() == null ? null : q.education().hhId);

        return resp.items() == null ? List.of()
                : java.util.Arrays.stream(resp.items()).map(mapper::toResume).toList();
    }
}