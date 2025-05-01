package com.example.vacancy_aggregator.service.impl.resume;

import com.example.vacancy_aggregator.client.sj.SjResumeFeign;
import com.example.vacancy_aggregator.data.Resume;
import com.example.vacancy_aggregator.data.util.SjResumeMapper;
import com.example.vacancy_aggregator.service.ResumeProvider;
import com.example.vacancy_aggregator.service.ResumeQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SjResumeProvider implements ResumeProvider {

    private final SjResumeFeign client;
    private final SjResumeMapper mapper;

    @Override
    public String providerName() {
        return "sj";
    }

    @Override
    @RateLimiter(name = "sj")
    public List<Resume> search(ResumeQuery q) {
        var resp = client.search(q.text(), q.page(), q.perPage());
        return resp.objects() == null ? List.of()
                : Arrays.stream(resp.objects()).map(mapper::toResume).toList();
    }
}