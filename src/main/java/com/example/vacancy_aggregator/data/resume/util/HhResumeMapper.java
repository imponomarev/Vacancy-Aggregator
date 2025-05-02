package com.example.vacancy_aggregator.data.resume.util;

import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.dto.hh.HhResumeSearchResponse.Item;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        imports = {
                OffsetDateTime.class,
                ZoneOffset.class,
                LocalDate.class,
                DateTimeFormatter.class,
                Resume.ExperienceEntry.class})
public interface HhResumeMapper {

    @Mapping(target = "source", constant = "hh")
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "position", source = "title")
    @Mapping(target = "city", source = "area.name")
    @Mapping(target = "salary", source = "salary.amount")
    @Mapping(target = "currency", source = "salary.currency")
    @Mapping(target = "updatedAt",
            expression = "java(OffsetDateTime.parse(item.updatedAt()))")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "experienceMonths", source = "totalExperience.months")
    @Mapping(target = "gender", source = "gender.id")
    @Mapping(target = "educationLevel", source = "education.level.name")
    @Mapping(target = "experience",
            expression = """
                java(item.experience() == null
                     ? List.<ExperienceEntry>of()
                     : item.experience().stream()
                        .map(e -> new ExperienceEntry(
                            e.company(),
                            e.position(),
                            e.start() == null ? null :
                                LocalDate.parse(e.start(), DateTimeFormatter.ISO_LOCAL_DATE)
                                         .atStartOfDay()
                                         .atOffset(ZoneOffset.UTC),
                            e.end()   == null ? null :
                                LocalDate.parse(e.end(),   DateTimeFormatter.ISO_LOCAL_DATE)
                                         .atStartOfDay()
                                         .atOffset(ZoneOffset.UTC),
                            e.description()
                        ))
                        .toList()
                )
             """)
    Resume toResume(Item item);
}