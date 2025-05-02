package com.example.vacancy_aggregator.favorite.resume.service;

import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.favorite.resume.entity.UserResumeFavorite;

import java.util.List;

public final class ResumeFavoriteMapper {

    private ResumeFavoriteMapper() { /* static only */ }

    /**
     * Строит новую сущность избранного из Resume и User.
     */
    public static UserResumeFavorite toEntity(Resume r, com.example.vacancy_aggregator.auth.entity.User user) {
        UserResumeFavorite f = new UserResumeFavorite();
        f.setUser(user);
        f.setSource(r.source());
        f.setExternalId(r.externalId());
        f.setFirstName(r.firstName());
        f.setLastName(r.lastName());
        f.setPosition(r.position());
        f.setSalary(r.salary());
        f.setCurrency(r.currency());
        f.setCity(r.city());
        f.setUpdatedAt(r.updatedAt());
        f.setUrl(r.url());
        f.setAge(r.age());
        f.setExperienceMonths(r.experienceMonths());
        f.setGender(r.gender());
        f.setEducationLevel(r.educationLevel());

        List<UserResumeFavorite.ExperienceEntry> exp = r.experience().stream()
                .map(e -> new UserResumeFavorite.ExperienceEntry(
                        e.company(),
                        e.position(),
                        e.startDate(),
                        e.endDate(),
                        e.description()
                ))
                .toList();
        f.setExperience(exp);

        return f;
    }

    /**
     * Преобразует сохранённую сущность в объект Resume для вывода клиенту.
     */
    public static Resume toDto(UserResumeFavorite f) {
        List<Resume.ExperienceEntry> exp = f.getExperience().stream()
                .map(e -> new Resume.ExperienceEntry(
                        e.getCompany(),
                        e.getPosition(),
                        e.getStartDate(),
                        e.getEndDate(),
                        e.getDescription()
                ))
                .toList();

        return new Resume(
                f.getSource(),
                f.getExternalId(),
                f.getFirstName(),
                f.getLastName(),
                f.getPosition(),
                f.getSalary(),
                f.getCurrency(),
                f.getCity(),
                f.getUpdatedAt(),
                f.getUrl(),
                f.getAge(),
                f.getExperienceMonths(),
                f.getGender(),
                f.getEducationLevel(),
                exp
        );
    }
}