package com.example.vacancy_aggregator.service.impl.resume;

import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.service.ResumeProvider;
import com.example.vacancy_aggregator.service.ResumeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeSearchService {

    private final List<ResumeProvider> providers;

    public List<Resume> search(ResumeQuery query) {
        boolean isPro = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_PRO"));

        if (!isPro) {
            throw new AccessDeniedException("Resume search is available for PRO users only");
        }

        return providers.stream()
                .flatMap(p -> p.search(query).stream())
                .toList();
    }
}