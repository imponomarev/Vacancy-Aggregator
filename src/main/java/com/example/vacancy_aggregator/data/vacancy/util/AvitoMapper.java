package com.example.vacancy_aggregator.data.vacancy.util;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.dto.avito.AvitoItemResponse;
import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse;
import org.mapstruct.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        imports = {OffsetDateTime.class, DateTimeFormatter.class})
public interface AvitoMapper {

    /* ---------- Item из списка ---------- */
    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    @Mapping(target = "source", constant = "avito")
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "company",
            expression = "java( item.employer()!=null ? item.employer().name() : null)")
    @Mapping(target = "city",
            expression = "java( item.location()!=null ? item.location().city() : null)")
    @Mapping(target = "salaryFrom", source = "salaryMin")
    @Mapping(target = "salaryTo", source = "salaryMax")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "publishedAt",
            expression = "java(parse(item.publishedAt()))")
    @Mapping(target = "url",
            expression = "java(\"https://www.avito.ru/vacancy/\" + item.id())")
    Vacancy toVacancy(AvitoSearchResponse.Item item);

    /* ---------- карточка по ID ---------- */
    @InheritInverseConfiguration(name = "toVacancy")
    Vacancy toVacancy(AvitoItemResponse item);

    /* ---------- util ---------- */
    default OffsetDateTime parse(String ts) {
        return ts == null ? null :
                OffsetDateTime.parse(ts, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
