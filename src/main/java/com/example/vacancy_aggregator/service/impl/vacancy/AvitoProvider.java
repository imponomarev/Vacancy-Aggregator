package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.client.avito.AvitoFeign;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.data.vacancy.util.AvitoMapper;
import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse;
import com.example.vacancy_aggregator.location.service.impl.AvitoLocationService;
import com.example.vacancy_aggregator.service.VacancyProvider;
import com.example.vacancy_aggregator.service.VacancyQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@link VacancyProvider} для поиска вакансий на платформе Авито.
 */
@Service
@RequiredArgsConstructor
public class AvitoProvider implements VacancyProvider {

    /**
     * Feign-клиент для обращения к Avito-API вакансий
     */
    private final AvitoFeign feign;
    /**
     * Маппер для конвертации данных из {@link AvitoSearchResponse} в {@link Vacancy}
     */
    private final AvitoMapper mapper;
    /**
     * Сервис разрешения текстовых названий регионов в Avito-ID
     */
    private final AvitoLocationService avitoLoc;

    @Override
    public String providerName() {
        return "avito";
    }

    /**
     * Выполняет поиск вакансий на Авито.
     * Разрешает переданный текстовый регион в числовой идентификатор.
     * Вызывает Feign-клиент {@link AvitoFeign#search(String, int, int, int)}.
     * Преобразует полученный список через {@link AvitoMapper} в {@link Vacancy}.
     *
     * @param query параметры поиска: текст запроса, страница, размер страницы, регион
     * @return список вакансий в формате {@link Vacancy}
     * @throws IllegalArgumentException если регион не найден в справочнике Avito
     */
    @Override
    @RateLimiter(name = "avito")
    public List<Vacancy> search(VacancyQuery query) {

        int region = avitoLoc.findRegionId(query.area())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Неизвестный для Авито регион: " + query.area()));

        AvitoSearchResponse resp = feign.search(
                query.text(),
                query.page(),
                query.perPage(),
                region);

        return resp.vacancies().stream()
                .map(mapper::toVacancy)
                .toList();
    }
}