package com.example.vacancy_aggregator.service.impl.resume;

import com.example.vacancy_aggregator.client.avito.AvitoResumeFeign;
import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.data.resume.util.AvitoResumeMapper;
import com.example.vacancy_aggregator.location.service.impl.LocationDirectory;
import com.example.vacancy_aggregator.service.ResumeProvider;
import com.example.vacancy_aggregator.service.ResumeQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvitoResumeProvider implements ResumeProvider {

    private final AvitoResumeFeign client;
    private final AvitoResumeMapper mapper;
    private final LocationDirectory locDir;

    @Override
    public String providerName() {
        return "avito";
    }

    @Override
    @RateLimiter(name = "avito")
    public List<Resume> search(ResumeQuery q) {

        int region = locDir.resolve(q.area())
                .flatMap(l -> l.avitoId().describeConstable())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown Avito region: " + q.area()));


        var resp = client.search(q.text(), q.page(), q.perPage(), region);

        return resp.resumes() == null ? List.of()
                : Arrays.stream(resp.resumes()).map(mapper::toResume).toList();
    }
}
