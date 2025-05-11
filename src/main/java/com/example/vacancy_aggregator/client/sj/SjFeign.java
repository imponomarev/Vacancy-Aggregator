package com.example.vacancy_aggregator.client.sj;

import feign.config.sj.SjVacancyFeignConfig;
import com.example.vacancy_aggregator.dto.sj.SjSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для SuperJob-API поиска вакансий.
 */
@FeignClient(name = "sj", url = "${sj.api.base-url}", configuration = SjVacancyFeignConfig.class)
public interface SjFeign {

    /**
     * Выполняет поиск вакансий на SuperJob.
     *
     * @param text        ключевые слова
     * @param town        название города
     * @param page        номер страницы
     * @param perPage     число элементов на странице
     * @param paymentFrom (необязательный) фильтр: оплата от
     * @param paymentTo   (необязательный) фильтр: оплата до
     * @param experience  (необязательный) фильтр: минимальный опыт (месяцы)
     * @return ответ {@link SjSearchResponse} с данными о вакансиях
     */
    @GetMapping("/2.0/vacancies/")
    SjSearchResponse search(
            @RequestParam("keyword") String text,
            @RequestParam("town") String town,
            @RequestParam("page") int page,
            @RequestParam("count") int perPage,
            @RequestParam(value = "payment_from", required = false) Integer paymentFrom,
            @RequestParam(value = "payment_to", required = false) Integer paymentTo,
            @RequestParam(value = "experience", required = false) Integer experience);
}
