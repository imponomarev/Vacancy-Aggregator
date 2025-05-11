package com.example.vacancy_aggregator.favorite.resume.repository;

import com.example.vacancy_aggregator.favorite.resume.entity.UserResumeFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с избранными резюме
 */
public interface UserResumeFavoriteRepository
        extends JpaRepository<UserResumeFavorite, Long> {

    Optional<UserResumeFavorite> findByUserIdAndSourceAndExternalId(Long userId,
                                                                    String source,
                                                                    String externalId);

    List<UserResumeFavorite> findAllByUserId(Long userId);

    void deleteByUserIdAndSourceAndExternalId(Long userId,
                                              String source,
                                              String externalId);
}
