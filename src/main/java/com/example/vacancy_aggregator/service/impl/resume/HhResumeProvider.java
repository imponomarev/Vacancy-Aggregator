package com.example.vacancy_aggregator.service.impl.resume;

import com.example.vacancy_aggregator.client.hh.HhResumeFeign;
import com.example.vacancy_aggregator.data.Resume;
import com.example.vacancy_aggregator.data.util.HhResumeMapper;
import com.example.vacancy_aggregator.location.service.impl.LocationDirectory;
import com.example.vacancy_aggregator.service.ResumeProvider;
import com.example.vacancy_aggregator.service.ResumeQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HhResumeProvider implements ResumeProvider {

    private final HhResumeFeign client;
    private final HhResumeMapper mapper;
    private final LocationDirectory locDir;

    @Value("${hh.api.app-token}")
    private String rawHhToken;

    private String bearer() {
        return rawHhToken.startsWith("Bearer ") ? rawHhToken : "Bearer " + rawHhToken;
    }

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

        var resp = client.search(q.text(), hhArea, q.page(), q.perPage(), bearer());

        return resp.items() == null ? List.of()
                : java.util.Arrays.stream(resp.items()).map(mapper::toResume).toList();
    }
}