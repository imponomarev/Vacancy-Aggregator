package com.example.vacancy_aggregator.dto.sj;

import java.util.List;

public record SjSearchResponse(int total, List<SjVacancy> objects) {

    public record SjVacancy(
            String id,
            String profession,
            Integer payment_from,
            Integer payment_to,
            String currency,
            String firm_name,
            Town town,
            long date_published,
            String link) {
        public record Town(Integer id, String title) {
        }
    }
}

