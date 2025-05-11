package com.example.vacancy_aggregator.service.util;

/**
 * Тип занятости (график работы) для резюме.
 */
public enum ResumeSchedule {
    FULL_DAY("fullDay", 6, "full-day"),
    SHIFT("shift", 12, "shift"),
    FLEXIBLE("flexible", 10, "flexible"),
    REMOTE("remote", 9, "remote"),
    FLY_IN_FLY_OUT("flyInFlyOut", 7, "fly-in-fly-out"),
    PARTIAL_DAY("partialDay", 13, "partial-day");

    public final String hhId;
    public final int sjId;
    public final String avitoId;

    ResumeSchedule(String hhId, int sjId, String avitoId) {
        this.hhId = hhId;
        this.sjId = sjId;
        this.avitoId = avitoId;
    }
}
