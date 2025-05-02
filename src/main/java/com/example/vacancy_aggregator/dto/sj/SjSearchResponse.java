package com.example.vacancy_aggregator.dto.sj;

import java.util.List;
import java.util.Map;

public record SjSearchResponse(int total, List<SjVacancy> objects) {

    public record SjVacancy(
            String id,
            String profession,
            Integer payment_from,
            Integer payment_to,
            String currency,
            String firm_name,
            Map<String, String> type_of_work,
            Map<String, String> experience,
            Town town,
            String work,
            long date_published,
            String link) {
        public record Town(Integer id, String title) {
        }
    }
}

