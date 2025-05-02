package com.example.vacancy_aggregator.data.resume.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class SjMapperUtils {
    private SjMapperUtils() {
    }

    public static OffsetDateTime toDateTime(Integer year, Integer month) {
        if (year == null || month == null) {
            return null;
        }
        return OffsetDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    }
}