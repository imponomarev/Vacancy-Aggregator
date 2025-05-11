package com.example.vacancy_aggregator.favorite.resume.service;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.favorite.resume.repository.UserResumeFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с избранными резюме.
 * Обеспечивает добавление, удаление и получение списка избранных резюме.
 * Доступен только пользователям с ролью PRO.
 */
@Service
@RequiredArgsConstructor
public class ResumeFavoriteService {

    private final UserResumeFavoriteRepository repository;

    /**
     * Добавляет резюме в избранное.
     *
     * @param user текущий пользователь
     * @param r    DTO резюме
     */
    @Transactional
    public void like(User user, Resume r) {
        requirePro();
        repository.findByUserIdAndSourceAndExternalId(user.getId(), r.source(), r.externalId())
                .orElseGet(() -> repository.save(
                        ResumeFavoriteMapper.toEntity(r, user)
                ));
    }

    /**
     * Удаляет резюме из избранного.
     *
     * @param user       текущий пользователь
     * @param source     код провайдера резюме
     * @param externalId внешний идентификатор резюме
     */
    @Transactional
    public void unlike(User user, String source, String externalId) {
        requirePro();
        repository.deleteByUserIdAndSourceAndExternalId(user.getId(), source, externalId);
    }

    /**
     * Возвращает все избранные резюме пользователя.
     *
     * @param user текущий пользователь
     * @return список DTO резюме
     */
    @Transactional(readOnly = true)
    public List<Resume> list(User user) {
        requirePro();
        return repository.findAllByUserId(user.getId())
                .stream()
                .map(ResumeFavoriteMapper::toDto)
                .toList();
    }

    /**
     * Проверяет, что текущий пользователь имеет роль PRO.
     *
     * @throws AccessDeniedException если роль не PRO
     */
    private void requirePro() {
        boolean pro = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_PRO"));
        if (!pro) {
            throw new AccessDeniedException("Resume likes available for PRO users only");
        }
    }
}
