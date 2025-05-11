package com.example.vacancy_aggregator.client.hh;

import com.example.vacancy_aggregator.dto.hh.HhSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для доступа к API поиска вакансий HeadHunter.
 * Делает запрос GET /vacancies к базе HH с параметрами поиска:
 * текст, регион, пагинация, минимальная зарплата и опыт.
 */
@FeignClient(name = "hh", url = "${hh.api.base-url}")
public interface HhFeign {

    /**
     * Выполняет поиск вакансий на hh.ru.
     *
     * @param text       ключевое слово для поиска
     * @param area       код региона в HH (получается через HH-suggest или кеш)
     * @param page       номер страницы
     * @param perPage    число вакансий на страницу
     * @param salary     (необязательный) минимальная зарплата в рублях
     * @param experience (необязательный) уровень опыта по справочнику HH
     * @return десериализованный ответ {@link HhSearchResponse} с полями:
     * общее число найденных, страниц, список элементов
     */
    @GetMapping("/vacancies")
    HhSearchResponse search(@RequestParam String text,
                            @RequestParam String area,
                            @RequestParam int page,
                            @RequestParam("per_page") int perPage,
                            @RequestParam(required = false) Integer salary,
                            @RequestParam(required = false) String experience);
}
