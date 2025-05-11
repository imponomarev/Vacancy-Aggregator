package com.example.vacancy_aggregator.data.vacancy.util;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static com.example.vacancy_aggregator.dto.hh.HhSearchResponse.*;

/**
 * Маппер для преобразования {@link HhSearchResponse.HhVacancyItem}
 * во внутренний DTO {@link Vacancy}.
 */
@Mapper(componentModel = "spring")
public interface HhMapper {

    /**
     * Основной метод конвертации одной записи HH в Vacancy.
     * По умолчанию любой null-элемент внутри {@link HhVacancyItem}
     * будет заменён на default-значение (RETURN_DEFAULT).
     *
     * @param item исходный элемент HH API
     * @return собранный объект {@link Vacancy}
     */
    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    @Mapping(target = "source", constant = "hh")
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "company", expression = "java(item.employer() !=null ? (String) item.employer().get(\"name\") : null)")
    @Mapping(target = "companyUrl", expression = "java((String)item.employer().get(\"alternate_url\"))")
    @Mapping(target = "city", expression = "java(item.area() !=null ? (String) item.area().get(\"name\") : null)")
    @Mapping(target = "salaryFrom", expression = "java(item.salary() !=null ? (Integer) item.salary().get(\"from\") : null)")
    @Mapping(target = "salaryTo", expression = "java(item.salary() !=null ? (Integer) item.salary().get(\"to\") : null)")
    @Mapping(target = "currency", expression = "java(item.salary() !=null ? (String) item.salary().get(\"currency\") : null)")
    @Mapping(target = "description", expression = "java(item.snippet()!=null ? (String)item.snippet().get(\"responsibility\") : null)")
    @Mapping(target = "experienceReq", expression = "java(item.experience()!=null ? (String)item.experience().get(\"name\") : null)")
    @Mapping(target = "employmentType", expression = "java(item.employment()!=null ? (String)item.employment().get(\"name\") : null)")
    @Mapping(target = "schedule", expression = "java(item.schedule()!=null ? (String)item.schedule().get(\"name\") : null)")
    @Mapping(target = "publishedAt", source = "published_at")
    @Mapping(target = "url", source = "alternate_url")
    Vacancy toVacancy(HhVacancyItem item);


    /**
     * Вспомогательный метод для парсинга строки published_at в OffsetDateTime.
     * Поддерживает формат ISO_LOCAL_DATE_TIME + смещение +HHmm.
     *
     * @param publishedAt строка с датой из HH API
     * @return OffsetDateTime или null если строка пустая/null
     */
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
