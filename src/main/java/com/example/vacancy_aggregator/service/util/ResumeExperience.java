package com.example.vacancy_aggregator.service.util;

public enum ResumeExperience {
    NO_EXPERIENCE("noExperience", 0, 0),
    BETWEEN_1_AND_3_YEARS("between1And3", 12, 36),
    BETWEEN_3_AND_6_YEARS("between3And6", 36, 72),
    MORE_THAN_6_YEARS("moreThan6", 72, 600);

    public final String hhId;
    public final int sjFrom;   // months
    public final int sjTo;     // months

    ResumeExperience(String hhId, int sjFrom, int sjTo) {
        this.hhId = hhId;
        this.sjFrom = sjFrom;
        this.sjTo = sjTo;
    }
}
