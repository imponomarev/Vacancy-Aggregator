package com.example.vacancy_aggregator.service;

import com.example.vacancy_aggregator.data.resume.Resume;
import java.util.List;

/**
 * Интерфейс для реализации коннектора к API конкретного провайдера резюме.
 */
public interface ResumeProvider {

    /**
     * Уникальное имя провайдера (например, "hh", "sj", "avito").
     */
    String providerName();

    /**
     * Запускает поиск резюме по параметрам.
     *
     * @param query параметры поиска
     * @return список резюме в формате {@link Resume}
     */
    List<Resume> search(ResumeQuery query);
}
