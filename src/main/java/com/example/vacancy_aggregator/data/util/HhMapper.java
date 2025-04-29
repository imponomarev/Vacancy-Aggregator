package com.example.vacancy_aggregator.data.util;

import com.example.vacancy_aggregator.data.Vacancy;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static com.example.vacancy_aggregator.dto.hh.HhSearchResponse.*;

@Mapper(componentModel = "spring")
public interface HhMapper {

    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    @Mapping(target = "source", constant = "hh")
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "company", expression = "java(item.employer() !=null ? (String) item.employer().get(\"name\") : null)")
    @Mapping(target = "city", expression = "java(item.area() !=null ? (String) item.area().get(\"name\") : null)")
    @Mapping(target = "salaryFrom", expression = "java(item.salary() !=null ? (Integer) item.salary().get(\"from\") : null)")
    @Mapping(target = "salaryTo", expression   = "java(item.salary() !=null ? (Integer) item.salary().get(\"to\") : null)")
    @Mapping(target = "currency", expression   = "java(item.salary() !=null ? (String) item.salary().get(\"currency\") : null)")
    @Mapping(target = "publishedAt", source = "published_at")
    @Mapping(target = "url", source = "alternate_url")
    Vacancy toVacancy(HhVacancyItem item);



    default OffsetDateTime mapPublishedAt(String publishedAt) {

        DateTimeFormatter flexFmt = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .appendOffset("+HHmm", "Z")
                .toFormatter();

        return (publishedAt == null || publishedAt.isEmpty())
                ? null
                : OffsetDateTime.parse(publishedAt, flexFmt);
    }
}
