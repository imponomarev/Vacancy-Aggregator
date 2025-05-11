package com.example.vacancy_aggregator.data.vacancy.util;

import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.example.vacancy_aggregator.dto.sj.SjSearchResponse.*;

/**
 * Маппер для преобразования элементов {@link SjSearchResponse.SjVacancy}
 * во внутреннюю модель {@link Vacancy}.
 */
@Mapper(componentModel = "spring",
        imports = {
                OffsetDateTime.class,
                Instant.class,
                ZoneOffset.class
        })
public interface SjMapper {

    /**
     * Конвертация полей SuperJob → Vacancy.
     * Несопоставленные поля (companyUrl, schedule) игнорируются.
     *
     * @param obj исходная запись {@link SjVacancy}
     * @return готовый объект {@link Vacancy}
     */
    @Mapping(target = "source", constant = "sj")
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "title", source = "profession")
    @Mapping(target = "company", source = "firm_name")
    @Mapping(target = "companyUrl", ignore = true)
    @Mapping(target = "city", expression = "java(obj.town() != null ? obj.town().title() : null)")
    @Mapping(target = "salaryFrom", source = "payment_from")
    @Mapping(target = "salaryTo", source = "payment_to")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "description", source = "work")
    @Mapping(target = "experienceReq", expression = "java(obj.experience()!=null ? obj.experience().get(\"title\") : null)")
    @Mapping(target = "employmentType", expression = "java(obj.type_of_work()!=null ? obj.type_of_work().get(\"title\") : null)")
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "publishedAt", expression =
            "java(OffsetDateTime.ofInstant(Instant.ofEpochSecond(obj.date_published()), "
                    + "ZoneOffset.UTC))")
    @Mapping(target = "url", source = "link")
    Vacancy toVacancy(SjVacancy obj);
}
