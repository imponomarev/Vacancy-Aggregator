package com.example.vacancy_aggregator.dto.avito;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO-ответ Avito Resume API.
 *
 * @param meta    метаданные ответа
 * @param resumes массив найденных резюме
 */
public record AvitoResumeSearchResponse(
        Meta meta,
        @JsonProperty("resumes") Item[] resumes
) {
    /**
     * Одна запись резюме.
     *
     * @param id              идентификатор резюме
     * @param title           заголовок/должность
     * @param salary          ожидаемая зарплата
     * @param location        информация о регионе (DTO {@link Location})
     * @param updatedAt       дата последнего обновления (ISO-строка)
     * @param age             возраст соискателя
     * @param totalExperience общий стаж в годах
     * @param gender          пол
     * @param educationLevel  уровень образования
     */
    public record Item(
            String id,
            String title,
            @JsonProperty("salary") Integer salary,
            Location location,
            @JsonProperty("updated") String updatedAt,
            Integer age,
            @JsonProperty("total_experience") Integer totalExperience,
            String gender,
            @JsonProperty("education_level") String educationLevel
    ) {
        public record Location(String title) {
        }
    }
    /**
     * Метаданные запроса.
     *
     * @param page      текущая страница
     * @param pages     всего страниц
     * @param per_page  элементов на страницу
     * @param cursor    курсор для постраничного перехода
     */
    public record Meta(int page, int pages, int per_page, long cursor) {
    }
}