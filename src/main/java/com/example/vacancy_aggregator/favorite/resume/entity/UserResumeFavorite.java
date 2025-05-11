package com.example.vacancy_aggregator.favorite.resume.entity;

import com.example.vacancy_aggregator.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность «избранное резюме» для пользователя.
 */
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

    @Column(name = "age")
    private Integer age;

    @Column(name = "experience_months")
    Integer experienceMonths;

    String gender;

    @Column(name = "education_level")
    String educationLevel;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /**
     * Хронология опыта работы,
     * сохраняется в отдельной таблице user_resume_experience.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_resume_experience",
            joinColumns = @JoinColumn(name = "favorite_id", referencedColumnName = "id"),
            foreignKey = @ForeignKey(name = "fk_resume_exp_to_favorite")
    )

    @AttributeOverrides({
            @AttributeOverride(name = "company", column = @Column(name = "company", length = 256)),
            @AttributeOverride(name = "position", column = @Column(name = "exp_position", length = 256)),
            @AttributeOverride(name = "startDate", column = @Column(name = "start_date")),
            @AttributeOverride(name = "endDate", column = @Column(name = "end_date")),
            @AttributeOverride(name = "description", column = @Column(name = "description", columnDefinition = "TEXT"))
    })
    private List<ExperienceEntry> experience = new ArrayList<>();

    /**
     * Встроенный класс для хранения одной записи опыта работы.
     */
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceEntry {
        private String company;
        private String position;
        private OffsetDateTime startDate;
        private OffsetDateTime endDate;
        private String description;
    }
}
