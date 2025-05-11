package com.example.vacancy_aggregator.data.resume.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Утилитный класс для преобразования года и месяца в {@link OffsetDateTime}.
 */
public final class SjMapperUtils {
    private SjMapperUtils() {
    }

    /**
     * Преобразует год и месяц в объект {@link OffsetDateTime} с днём = 1
     * и часовым поясом UTC.
     *
     * @param year  год (например, 2021)
     * @param month номер месяца (1–12)
     * @return {@link OffsetDateTime} первого числа указанного месяца и года в UTC,
     * или {@code null}, если один из параметров равен {@code null}.
     */
    public static OffsetDateTime toDateTime(Integer year, Integer month) {
        if (year == null || month == null) {
            return null;
        }
        return OffsetDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    }
}