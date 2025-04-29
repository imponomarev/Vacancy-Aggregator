package com.example.vacancy_aggregator.data.util;

import com.example.vacancy_aggregator.data.Resume;
import com.example.vacancy_aggregator.dto.hh.HhResumeSearchResponse.Item;
import org.mapstruct.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        imports = {OffsetDateTime.class, DateTimeFormatter.class})
public interface HhResumeMapper {

    @Mapping(target = "source", constant = "hh")
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "position", source = "title")
    @Mapping(target = "city", source = "area.name")
    @Mapping(target = "salary", source = "salary.amount")
    @Mapping(target = "currency", source = "salary.currency")
    @Mapping(target = "updatedAt",
            expression = "java(OffsetDateTime.parse(item.updatedAt()))")
    @Mapping(target = "url",
            expression = "java(\"https://hh.ru/resume/\"+item.id())")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    Resume toResume(Item item);
}