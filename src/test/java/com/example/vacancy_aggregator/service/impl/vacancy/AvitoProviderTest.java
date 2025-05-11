package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.client.avito.AvitoFeign;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.data.vacancy.util.AvitoMapper;
import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse;
import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse.Item;
import com.example.vacancy_aggregator.dto.avito.AvitoSearchResponse.Item.AddressDetails;
import com.example.vacancy_aggregator.location.service.impl.AvitoLocationService;
import com.example.vacancy_aggregator.service.VacancyQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvitoProviderTest {

    @Mock private AvitoFeign feign;
    @Mock private AvitoMapper mapper;
    @Mock private AvitoLocationService avitoLoc;

    private AvitoProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        provider = new AvitoProvider(feign, mapper, avitoLoc);
    }

    @Test
    void whenRegionFound_thenSearchAndMap() {
        VacancyQuery query = new VacancyQuery("devops", 3, 2, "Москва", null, null, null, null);
        when(avitoLoc.findRegionId("Москва")).thenReturn(Optional.of(637640));

        // в правильном порядке: id, title, profession, companyName, addressDetails, published_at, link
        AddressDetails addr = new AddressDetails("ул. Ленина", "Москва");
        Item raw = new Item(
                "555",                          // id
                "DevOps Engineer",              // title
                "Инженер поддержки",            // profession
                "OpsZone",                      // companyName
                addr,                           // addressDetails
                "2025-05-10T10:00:00Z",         // published_at
                "https://avito.example/v/555"   // link
        );
        AvitoSearchResponse resp = new AvitoSearchResponse(
                new AvitoSearchResponse.Meta(3, 10, 2),
                List.of(raw)
        );
        when(feign.search("devops", 3, 2, 637640)).thenReturn(resp);

        Vacancy vac = Vacancy.builder()
                .source("avito")
                .externalId("555")
                .title("DevOps Engineer")
                .company("OpsZone")
                .city("Москва")
                .url("https://avito.example/v/555")
                .build();
        when(mapper.toVacancy(raw)).thenReturn(vac);

        List<Vacancy> result = provider.search(query);
        assertEquals(1, result.size());
        assertSame(vac, result.get(0));
        verify(feign).search("devops", 3, 2, 637640);
    }

    @Test
    void whenRegionNotFound_thenThrows() {
        when(avitoLoc.findRegionId("Nowhere")).thenReturn(Optional.empty());
        VacancyQuery query = new VacancyQuery("x", 0, 1, "Nowhere", null, null, null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> provider.search(query));
        assertTrue(ex.getMessage().contains("Неизвестный для Авито регион"));
    }
}
