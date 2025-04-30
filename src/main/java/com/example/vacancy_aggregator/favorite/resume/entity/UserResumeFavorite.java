package com.example.vacancy_aggregator.favorite.resume.entity;

import com.example.vacancy_aggregator.auth.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_resume_favorites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "source", "external_id"}))
@Data
public class UserResumeFavorite {

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

    private String firstName;
    private String lastName;
    private String position;
    private Integer salary;
    private String currency;
    private String city;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    private String url;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
