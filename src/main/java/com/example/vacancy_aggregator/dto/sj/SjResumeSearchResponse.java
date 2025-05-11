package com.example.vacancy_aggregator.dto.sj;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO-ответ SuperJob Resume API.
 *
 * @param objects массив найденных резюме
 * @param total   общее число найденных резюме
 */
public record SjResumeSearchResponse(
        @JsonProperty("objects") Item[] objects,
        int total
) {
    /**
     * Вложенный DTO для одной записи резюме.
     *
     * @param id                   уникальный ID резюме
     * @param firstName            имя соискателя
     * @param lastName             фамилия соискателя
     * @param position             должность
     * @param town                 город (DTO {@link Town})
     * @param payment              ожидаемая зарплата
     * @param currency             валюта
     * @param updatedAt            время последнего изменения (epoch seconds)
     * @param age                  возраст
     * @param experienceMonthTotal общий опыт работы в месяцах
     * @param link                 ссылка на резюме
     * @param gender               пол (DTO {@link Gender})
     * @param education            образование (DTO {@link Education})
     * @param workHistory          история работ (список DTO {@link WorkHistory})
     */
    public record Item(
            @JsonProperty("id") long id,
            @JsonProperty("firstname") String firstName,
            @JsonProperty("lastname") String lastName,
            @JsonProperty("profession") String position,
            @JsonProperty("town") Town town,
            @JsonProperty("payment") Integer payment,
            @JsonProperty("currency") String currency,
            @JsonProperty("date_last_modified") long updatedAt,
            Integer age,
            @JsonProperty("experience_month_total") Integer experienceMonthTotal,
            String link,
            Gender gender,
            Education education,
            @JsonProperty("work_history") List<WorkHistory> workHistory
    ) {
        public record Town(String title) {
        }

        public record Gender(@JsonProperty("title") String title) {
        }

        public record Education(String title) {
        }

        public record WorkHistory(
                String name,
                String profession,
                String work,
                Integer monthbeg,
                Integer monthend,
                Integer yearbeg,
                Integer yearend
        ) {
        }
    }
}
