package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.client.hh.HhFeign;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.data.vacancy.util.HhMapper;
import com.example.vacancy_aggregator.dto.hh.HhSearchResponse;
import com.example.vacancy_aggregator.location.service.impl.LocationDirectory;
import com.example.vacancy_aggregator.location.data.Location;
import com.example.vacancy_aggregator.service.VacancyQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HhProviderTest {

    @Mock private HhFeign client;
    @Mock private HhMapper mapper;
    @Mock private LocationDirectory locDir;

    private HhProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        provider = new HhProvider(client, mapper, locDir);
    }

    @Test
    void whenAreaResolvesAndApiReturns_thenMapsAllItems() {
        // given
        VacancyQuery query = new VacancyQuery("java", 1, 5, "Москва", null, null, null, null);
        // LocationDirectory.resolve("Москва") -> Location with hhId="77"
        when(locDir.resolve("Москва"))
                .thenReturn(Optional.of(new Location("77", null, null, "Москва")));
        // Feign возвращает один элемент
        HhSearchResponse.HhVacancyItem raw = new HhSearchResponse.HhVacancyItem(
                "123", "Java Dev",
                Map.of("from", 100000, "to", 150000, "currency", "RUR"),
                Map.of("name", "Acme", "alternate_url", "http://..."),
                Map.of("name", "Москва"), null, null, null, null,
                "2025-05-10T12:00:00+0300", "http://..."
        );
        HhSearchResponse resp = new HhSearchResponse(1, 1, List.of(raw));
        when(client.search("java", "77", 1, 5, null, null))
                .thenReturn(resp);

        // и маппер преобразует raw -> vacancy
        Vacancy vac = Vacancy.builder()
                .source("hh")
                .externalId("123")
                .title("Java Dev")
                .company("Acme")
                .city("Москва")
                .salaryFrom(100000)
                .salaryTo(150000)
                .currency("RUR")
                .url("http://...")
                .build();
        when(mapper.toVacancy(raw)).thenReturn(vac);

        // when
        List<Vacancy> result = provider.search(query);

        // then
        assertEquals(1, result.size());
        assertSame(vac, result.get(0));

        // verify Feign вызов
        verify(client).search("java", "77", 1, 5, null, null);
    }

    @Test
    void whenAreaNotFound_thenThrows() {
        // given
        when(locDir.resolve("Unknown")).thenReturn(Optional.empty());
        VacancyQuery query = new VacancyQuery("x", 0, 10, "Unknown", null, null, null, null);

        // then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> provider.search(query));
        assertTrue(ex.getMessage().contains("Не удалось определить HH-region-id"));
    }
}
