package com.example.vacancy_aggregator.data.resume;

import java.time.OffsetDateTime;
import java.util.List;

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
    public record ExperienceEntry(
            String company,
            String position,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            String description
    ) {
    }
}