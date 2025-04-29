package com.example.vacancy_aggregator.data;

import java.time.OffsetDateTime;

public record Resume(
        String   source,        // hh | sj | avito
        String   externalId,
        String   firstName,
        String   lastName,
        String   position,
        Integer  salary,
        String   currency,
        String   city,
        OffsetDateTime updatedAt,
        String   url
) {}