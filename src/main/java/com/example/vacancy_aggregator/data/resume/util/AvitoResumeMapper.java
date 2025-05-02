package com.example.vacancy_aggregator.data.resume.util;

import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.dto.avito.AvitoResumeSearchResponse.Item;
import org.mapstruct.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        imports = {OffsetDateTime.class, DateTimeFormatter.class})
public interface AvitoResumeMapper {

    @Mapping(target = "source", constant = "avito")
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "position", source = "title")
    @Mapping(target = "salary", source = "salary")
    @Mapping(target = "currency", constant = "rub")
    @Mapping(target = "city", source = "location.title")
    @Mapping(target = "updatedAt",
            expression = "java(OffsetDateTime.parse(item.updatedAt()))")
    @Mapping(target = "url",
            expression = "java(\"https://www.avito.ru/resume/\" + item.id())")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "experienceMonths", source = "totalExperience")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "educationLevel", source = "educationLevel")
    @Mapping(target = "experience", expression = "java(List.of())")
    Resume toResume(Item item);
}
