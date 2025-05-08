package com.example.vacancy_aggregator.service.util;

import lombok.Getter;

/**
 * Унифицированный перечень вариантов опыта работы, пригодный
 * для конвертации как в HH, так и в SuperJob.
 */
@Getter
public enum ExperienceLevel {

    NO_EXPERIENCE("noExperience", 1),
    BETWEEN_1_AND_3("between1And3", 2),
    BETWEEN_3_AND_6("between3And6", 3),
    MORE_THAN_6("moreThan6", 4);

    private final String hhCode;   // строковый id для HH
    private final int sjCode;      // числовой id для SJ

    ExperienceLevel(String hhCode, int sjCode) {
        this.hhCode = hhCode;
        this.sjCode = sjCode;
    }

    public String toHh() {
        return hhCode;
    }

    public Integer toSj() {
        return sjCode;
    }
}
