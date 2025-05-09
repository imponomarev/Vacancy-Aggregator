package com.example.vacancy_aggregator.service.util;

public enum ResumeEducation {
    SECONDARY("secondary", 5, "secondary"),
    SPECIAL_SECONDARY("special_secondary", 4, "special-secondary"),
    UNFINISHED_HIGHER("unfinished_higher", 3, "unfinished-higher"),
    HIGHER("higher", 2, "higher"),
    BACHELOR("bachelor", 2, "higher"),
    MASTER("master", 2, "higher");

    public final String hhId;
    public final int sjId;
    public final String avitoId;

    ResumeEducation(String hhId, int sjId, String avitoId) {
        this.hhId = hhId;
        this.sjId = sjId;
        this.avitoId = avitoId;
    }
}
