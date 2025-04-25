package com.example.vacancy_aggregator.data.util;

import com.example.vacancy_aggregator.data.Vacancy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.example.vacancy_aggregator.dto.SjSearchResponse.*;


@Mapper(componentModel="spring",
        imports = {
                OffsetDateTime.class,
                Instant.class,
                ZoneOffset.class
        })
public interface SjMapper {
    @Mapping(target="source",     constant="sj")
    @Mapping(target="externalId", source="id")
    @Mapping(target="title",      source="profession")
    @Mapping(target="company",    source="firm_name")
    @Mapping(target = "city", expression = "java(obj.town() != null ? obj.town().title() : null)")
    @Mapping(target="salaryFrom", source="payment_from")
    @Mapping(target="salaryTo",   source="payment_to")
    @Mapping(target = "publishedAt", expression =
            "java(OffsetDateTime.ofInstant(Instant.ofEpochSecond(obj.date_published()), "
                    +   "ZoneOffset.UTC))")
    @Mapping(target="url",        source="link")
    Vacancy toVacancy(SjVacancy obj);
}
