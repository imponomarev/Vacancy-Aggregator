package com.example.vacancy_aggregator.dto;

import java.util.List;
import java.util.Map;

public record HhSearchResponse(
        int found,
        int pages,
        List<HhVacancyItem> items
) {
    public record HhVacancyItem(
            String id,
            String name,
            Map<String, Object> salary,
            Map<String, Object> employer,
            Map<String, Object> area,
            String published_at,
            String alternate_url
    ) {
    }
}
