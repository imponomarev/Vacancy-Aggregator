package com.example.vacancy_aggregator.favorite.vacancy.repository;

import com.example.vacancy_aggregator.favorite.vacancy.entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {

    Optional<UserFavorite> findByUserIdAndSourceAndExternalId(Long userId, String source, String externalId);

    List<UserFavorite> findAllByUserId(Long userId);

    void deleteByUserIdAndSourceAndExternalId(Long userId, String source, String externalId);
}
