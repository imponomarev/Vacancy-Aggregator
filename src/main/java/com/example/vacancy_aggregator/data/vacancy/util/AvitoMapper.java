package com.example.vacancy_aggregator.data.vacancy.util;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse;
import org.mapstruct.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        imports = {OffsetDateTime.class, DateTimeFormatter.class})
public interface AvitoMapper {

    @Mapping(target = "source", constant = "avito")
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "company", source = "companyName")
    @Mapping(target = "companyUrl", ignore = true)
    @Mapping(target = "city", source = "addressDetails.city")
    @Mapping(target = "description", source = "profession")
    @Mapping(target = "experienceReq", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "publishedAt",
            expression = "java(item.published_at()==null ? null : "
                    + "OffsetDateTime.parse(item.published_at(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))")
    @Mapping(target = "url", source = "link")
    Vacancy toVacancy(AvitoSearchResponse.Item item);


    default OffsetDateTime parse(String ts) {
        return ts == null ? null :
                OffsetDateTime.parse(ts, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
