package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.service.VacancyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.vacancy_aggregator.service.VacancyQuery;

import java.util.List;

/**
 * Сервис-агрегатор: запускает поиск по всем {@link VacancyProvider},
 * фильтрует по правам (ROLE_PRO → доступен avito) и по списку выбранных провайдеров.
 */
@Service
@RequiredArgsConstructor
public class VacancySearchService {

    private final List<VacancyProvider> providers;

    /**
     * Запускает поиск по всем доступным провайдерам и объединяет результаты.
     *
     * @param query параметры поиска
     * @return объединённый список вакансий
     */
    public List<Vacancy> search(VacancyQuery query) {

        boolean isPro = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PRO"));

        List<String> requested = query.providers();

        return providers.stream()
                .filter(p -> isPro || !"avito".equals(p.providerName()))
                .filter(p -> {
                    if (requested == null || requested.isEmpty()) {
                        return true;
                    }
                    return requested.contains(p.providerName());
                })
                .flatMap(p -> p.search(query).stream())
                .toList();
    }
}
