package com.example.vacancy_aggregator.service.impl.vacancy;

import com.example.vacancy_aggregator.client.sj.SjFeign;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.data.vacancy.util.SjMapper;
import com.example.vacancy_aggregator.dto.sj.SjSearchResponse;
import com.example.vacancy_aggregator.dto.sj.SjSearchResponse.SjVacancy;
import com.example.vacancy_aggregator.dto.sj.SjSearchResponse.SjVacancy.Town;
import com.example.vacancy_aggregator.location.data.Location;
import com.example.vacancy_aggregator.location.service.impl.LocationDirectory;
import com.example.vacancy_aggregator.service.VacancyQuery;
import com.example.vacancy_aggregator.service.util.ExperienceLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SuperJobProviderTest {

    @Mock private SjFeign client;
    @Mock private SjMapper mapper;
    @Mock private LocationDirectory locDir;

    private SuperJobProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        provider = new SuperJobProvider(client, mapper, locDir);
    }

    @Test
    void whenSjTownResolved_thenSearchAndMap() {
        // создаём VacancyQuery с ExperienceLevel
        VacancyQuery query = new VacancyQuery(
                "python", 2, 3, "СПб",
                null,
                50000, 100000,
                ExperienceLevel.BETWEEN_1_AND_3
        );
        // SJ-town из LocationDirectory
        when(locDir.resolve("СПб"))
                .thenReturn(Optional.of(new Location(null, 1L, null, "СПб")));

        // готовим «сырое» SJ-вакансию
        SjVacancy raw = new SjVacancy(
                "321",                       // id
                "Python Dev",                // profession
                50000,                       // payment_from
                100000,                      // payment_to
                "RUR",                       // currency
                "BetaCorp",                  // firm_name
                Map.of("id","полный","title","Полная занятость"),       // type_of_work
                Map.of("id","between1And3","title","От 1 до 3 лет"),    // experience
                new Town(1, "СПб"),          // town
                "coding...",                 // work
                1_600_000_000L,              // date_published
                "http://sj.example/dev/321"  // link
        );
        SjSearchResponse resp = new SjSearchResponse(1, List.of(raw));
        // ожидание вызова Feign
        when(client.search(
                "python", "1", 2, 3,
                50000, 100000,
                ExperienceLevel.BETWEEN_1_AND_3.toSj()
        )).thenReturn(resp);

        // и маппер
        Vacancy vac = Vacancy.builder()
                .source("sj")
                .externalId("321")
                .title("Python Dev")
                .company("BetaCorp")
                .city("СПб")
                .salaryFrom(50000)
                .salaryTo(100000)
                .currency("RUR")
                .url("http://sj.example/dev/321")
                .build();
        when(mapper.toVacancy(raw)).thenReturn(vac);

        // when
        List<Vacancy> result = provider.search(query);

        // then
        assertEquals(1, result.size());
        assertSame(vac, result.get(0));
        verify(client).search(
                "python", "1", 2, 3,
                50000, 100000,
                ExperienceLevel.BETWEEN_1_AND_3.toSj()
        );
    }

    @Test
    void whenNoSjTown_thenUseRawArea() {
        VacancyQuery query = new VacancyQuery(
                "go", 0, 1, "42", null,
                null, null,
                null
        );
        when(locDir.resolve("42")).thenReturn(Optional.empty());

        SjVacancy raw = new SjVacancy(
                "9", "Go Dev",
                null, null,
                null, null,
                Map.of(), Map.of(),
                new Town(null, null),
                null,
                0L,
                null
        );
        when(client.search("go", "42", 0, 1, null, null, null))
                .thenReturn(new SjSearchResponse(1, List.of(raw)));

        Vacancy vac = Vacancy.builder().source("sj").externalId("9").build();
        when(mapper.toVacancy(raw)).thenReturn(vac);

        List<Vacancy> result = provider.search(query);
        assertEquals(1, result.size());
        assertEquals("9", result.get(0).getExternalId());
    }
}

