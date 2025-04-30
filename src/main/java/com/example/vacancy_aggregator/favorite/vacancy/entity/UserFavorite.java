package com.example.vacancy_aggregator.favorite.vacancy.entity;

import com.example.vacancy_aggregator.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_favorites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "source", "external_id"}))
@Getter
@Setter
public class UserFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * "hh" | "sj" | "avito"
     */
    private String source;

    @Column(name = "external_id")
    private String externalId;

    private String title;
    private String company;
    private String city;

    @Column(name = "salary_from")
    private Integer salaryFrom;

    @Column(name = "salary_to")
    private Integer salaryTo;

    private String currency;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(name = "url")
    private String url;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
}