package com.example.vacancy_aggregator.service.impl.resume;

import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.service.ResumeProvider;
import com.example.vacancy_aggregator.service.ResumeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис-агрегатор, который запускает поиск резюме во всех
 * внедрённых {@link ResumeProvider} по запросу {@link ResumeQuery}.
 */
@Service
@RequiredArgsConstructor
public class ResumeSearchService {

    private final List<ResumeProvider> providers;

    /**
     * Выполняет поиск, фильтруя по списку провайдеров из запроса.
     *
     * @param query параметры поиска
     * @return объединённый список резюме
     */
    public List<Resume> search(ResumeQuery query) {
        boolean isPro = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_PRO"));

        if (!isPro) {
            throw new AccessDeniedException("Resume search is available for PRO users only");
        }

        return providers.stream()
                .filter(p -> query.providers() == null
                        || query.providers().isEmpty()
                        || query.providers().contains(p.providerName()))
                .flatMap(p -> p.search(query).stream())
                .toList();
    }
}