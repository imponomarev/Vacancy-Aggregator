package com.example.vacancy_aggregator.service.util;

/**
 * Уровень опыта работы для резюме.
 * Содержит параметры для каждого API:
 * - hhId    — строковое значение для hh.ru
 * - sjFrom  — минимальный опыт в месяцах для superjob.ru
 * - sjTo    — максимальный опыт в месяцах для superjob.ru
 */
public enum ResumeExperience {
    NO_EXPERIENCE("noExperience", 0, 0),
    BETWEEN_1_AND_3_YEARS("between1And3", 12, 36),
    BETWEEN_3_AND_6_YEARS("between3And6", 36, 72),
    MORE_THAN_6_YEARS("moreThan6", 72, 600);

    public final String hhId;
    public final int sjFrom;
    public final int sjTo;

    ResumeExperience(String hhId, int sjFrom, int sjTo) {
        this.hhId = hhId;
        this.sjFrom = sjFrom;
        this.sjTo = sjTo;
    }
}
