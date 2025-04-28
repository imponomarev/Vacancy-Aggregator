package com.example.vacancy_aggregator.favorite.service;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.data.Vacancy;
import com.example.vacancy_aggregator.favorite.entity.UserFavorite;
import com.example.vacancy_aggregator.favorite.repository.UserFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserFavoriteRepository repository;

    /**
     * Добавить лайк
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
                    f.setPublishedAt(v.getPublishedAt());
                    f.setUrl(v.getUrl());
                    return repository.save(f);
                });
    }

    /**
     * Удалить лайк
     */
    @Transactional
    public void unlike(User user, String source, String externalId) {
        repository.deleteByUserIdAndSourceAndExternalId(user.getId(), source, externalId);
    }

    /**
     * Получить все лайки текущего пользователя
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
                        .publishedAt(f.getPublishedAt())
                        .url(f.getUrl())
                        .build())
                .toList();
    }
}
