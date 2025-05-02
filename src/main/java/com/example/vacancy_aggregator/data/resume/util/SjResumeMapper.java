package com.example.vacancy_aggregator.data.resume.util;

import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.dto.sj.SjResumeSearchResponse.Item;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring",
        imports = {OffsetDateTime.class, Instant.class, ZoneOffset.class, Resume.ExperienceEntry.class})
public interface SjResumeMapper {

    @Mapping(target = "source", constant = "sj")
    @Mapping(target = "externalId", expression = "java(String.valueOf(item.id()))")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "salary", source = "payment")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "city", source = "town.title")
    @Mapping(target = "updatedAt",
            expression = "java(OffsetDateTime.ofInstant(Instant.ofEpochSecond(item.updatedAt()), ZoneOffset.UTC))")
    @Mapping(target = "url", source = "link")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "experienceMonths", source = "experienceMonthTotal")
    @Mapping(target = "gender", source = "gender.title")
    @Mapping(target = "educationLevel", source = "education.title")
    @Mapping(target = "experience",
            expression = """
                    java(item.workHistory()==null ? List.of() :
                         item.workHistory().stream()
                             .map(w -> new ExperienceEntry(
                                     w.name(),
                                     w.profession(),
                                     SjMapperUtils.toDateTime(w.yearbeg(), w.monthbeg()),
                                     SjMapperUtils.toDateTime(w.yearend(), w.monthend()),
                                     w.work()))
                             .toList())
                    """)
    Resume toResume(Item item);
}
