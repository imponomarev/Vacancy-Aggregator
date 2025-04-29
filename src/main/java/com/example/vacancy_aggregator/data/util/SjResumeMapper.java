package com.example.vacancy_aggregator.data.util;

import com.example.vacancy_aggregator.data.Resume;
import com.example.vacancy_aggregator.dto.sj.SjResumeSearchResponse.Item;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring",
        imports = {OffsetDateTime.class, Instant.class, ZoneOffset.class})
public interface SjResumeMapper {

    @Mapping(target = "source", constant = "sj")
    @Mapping(target = "externalId", expression = "java(String.valueOf(item.id()))")
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "position", source = "position")
    @Mapping(target = "salary", source = "salary")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "city", source = "town.title")
    @Mapping(target = "updatedAt",
            expression = "java(OffsetDateTime.ofInstant(Instant.ofEpochSecond(item.updatedAt()), ZoneOffset.UTC))")
    @Mapping(target = "url",
            expression = "java(\"https://r.superjob.ru/resume/cv-\" + item.id()+\".html\")")
    Resume toResume(Item item);
}
