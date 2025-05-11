package com.example.vacancy_aggregator.favorite.vacancy.service;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.favorite.vacancy.entity.UserFavorite;
import com.example.vacancy_aggregator.favorite.vacancy.repository.UserFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для управления «лайками» (избранными вакансиями) пользователя.
 */
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserFavoriteRepository repository;

    /**
     * Добавляет вакансию в избранные текущего пользователя.
     * Если такая вакансия уже есть, дубли не создаются.
     *
     * @param user объект {@link User} — текущий пользователь
     * @param v    объект {@link Vacancy} для добавления
     */
    @Transactional
    public void like(User user, Vacancy v) {
        repository.findByUserIdAndSourceAndExternalId(user.getId(), v.getSource(), v.getExternalId())
                .orElseGet(() -> {
                    UserFavorite f = new UserFavorite();
                    f.setUser(user);
                    f.setSource(v.getSource());
                    f.setExternalId(v.getExternalId());
                    f.setTitle(v.getTitle());
                    f.setCompany(v.getCompany());
                    f.setCity(v.getCity());
                    f.setSalaryFrom(v.getSalaryFrom());
                    f.setSalaryTo(v.getSalaryTo());
                    f.setCurrency(v.getCurrency());
                    f.setDescription(v.getDescription());
                    f.setExperienceReq(v.getExperienceReq());
                    f.setEmploymentType(v.getEmploymentType());
                    f.setSchedule(v.getSchedule());
                    f.setPublishedAt(v.getPublishedAt());
                    f.setUrl(v.getUrl());
                    return repository.save(f);
                });
    }

    /**
     * Убирает вакансию из избранных текущего пользователя.
     *
     * @param user       объект {@link User} — текущий пользователь
     * @param source     источник вакансии ("hh", "sj" или "avito")
     * @param externalId внешний идентификатор вакансии
     */
    @Transactional
    public void unlike(User user, String source, String externalId) {
        repository.deleteByUserIdAndSourceAndExternalId(user.getId(), source, externalId);
    }

    /**
     * Получает все избранные вакансии текущего пользователя.
     *
     * @param user объект {@link User} — текущий пользователь
     * @return список {@link Vacancy}, собранных из сохраняемых в БД сущностей
     */
    @Transactional(readOnly = true)
    public List<Vacancy> list(User user) {
        return repository.findAllByUserId(user.getId()).stream()
                .map(f -> Vacancy.builder()
                        .source(f.getSource())
                        .externalId(f.getExternalId())
                        .title(f.getTitle())
                        .company(f.getCompany())
                        .city(f.getCity())
                        .salaryFrom(f.getSalaryFrom())
                        .salaryTo(f.getSalaryTo())
                        .currency(f.getCurrency())
                        .description(f.getDescription())
                        .experienceReq(f.getExperienceReq())
                        .employmentType(f.getEmploymentType())
                        .schedule(f.getSchedule())
                        .publishedAt(f.getPublishedAt())
                        .url(f.getUrl())
                        .build())
                .toList();
    }
}
