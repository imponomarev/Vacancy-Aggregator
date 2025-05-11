package com.example.vacancy_aggregator.data.resume;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO для представления резюме независимо от провайдера.
 *
 * @param source            имя провайдера ("hh", "sj" или "avito")
 * @param externalId        ID резюме в системе провайдера
 * @param firstName         имя соискателя
 * @param lastName          фамилия соискателя
 * @param position          текущая или желаемая должность
 * @param salary            ожидаемая зарплата
 * @param currency          валюта зарплаты
 * @param city              город
 * @param updatedAt         время последнего обновления резюме
 * @param url               ссылка на полное резюме
 * @param age               возраст соискателя
 * @param experienceMonths  общий стаж в месяцах
 * @param gender            пол
 * @param educationLevel    уровень образования
 * @param experience        список прошлых мест работы
 */
public record Resume(
        String source,        // hh | sj | avito
        String externalId,
        String firstName,
        String lastName,
        String position,
        Integer salary,
        String currency,
        String city,
        OffsetDateTime updatedAt,
        String url,
        Integer age,
        Integer experienceMonths,
        String gender,
        String educationLevel,
        List<ExperienceEntry> experience
) {
    /**
     * Элемент истории опыта работы.
     *
     * @param company     название компании
     * @param position    должность
     * @param startDate   дата начала
     * @param endDate     дата окончания
     * @param description описание обязанностей
     */
    public record ExperienceEntry(
            String company,
            String position,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            String description
    ) {
    }
}