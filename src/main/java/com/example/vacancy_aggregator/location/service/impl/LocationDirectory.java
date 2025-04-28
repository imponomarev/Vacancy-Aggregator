package com.example.vacancy_aggregator.location.service.impl;

import com.example.vacancy_aggregator.client.hh.HhSuggestFeign;
import com.example.vacancy_aggregator.client.sj.SjDictFeign;
import com.example.vacancy_aggregator.location.data.Location;
import com.example.vacancy_aggregator.location.dto.SjTownResponse;
import com.example.vacancy_aggregator.location.dto.SuggestAreaResponse;
import com.example.vacancy_aggregator.location.service.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationDirectory {

    private final HhSuggestFeign hh;
    private final SjDictFeign sj;
    private final LocationRepository repo;
    private final AvitoLocationService avito;


    /**
     * Универсальный резолвинг с lazy-loading:
     * 1) ищем по hhId,
     * 2) по sjId,
     * 3) по имени,
     * 4) по avitoId,
     * 5) попытка через HH/SJ API.
     */
    public Optional<Location> resolve(String any) {
        // 1. По hh-ID (строка):
        var byHh = repo.byHhId(any);
        if (byHh.isPresent()) return byHh;

        // 2. По SJ-ID (число в строке):
        Optional<Location> bySj = tryParseLong(any)
                .flatMap(repo::bySjId);
        if (bySj.isPresent()) return bySj;

        // 3. По названию:
        String name = any.trim();
        var byName = repo.byName(name);
        if (byName.isPresent()) return byName;

        // 4) ищем по Avito-region-id
        Optional<Integer> byAvito = avito.findRegionId(name);
        if (byAvito.isPresent()) {
            int aid = byAvito.get();
            return repo.byAvitoId(aid)
                    .or(() -> {
                        // создаём и кешируем новую локацию, avitoId известен, остальные — null
                        Location loc = new Location(
                                null,     // hhId
                                null,     // sjId
                                aid,      // avitoId
                                name      // human-readable name
                        );
                        repo.save(loc);
                        return Optional.of(loc);
                    });
        }

        // 5) fallback: пробуем через HH / SJ API
        Location fetched = fetchAndCache(name);
        return Optional.ofNullable(fetched);


    }

    private Location fetchAndCache(String name) {
        // a) hh.suggest требует минимум 2 символа
        if (name.length() >= 2) {
            try {
                SuggestAreaResponse resp = hh.suggest(name, "RU");
                for (var item : resp.items()) {
                    if (item.text().equalsIgnoreCase(name)) {
                        String hhId = item.id();

                        // b) подгружаем SJ-ID по названию через sj.towns
                        SjTownResponse sjResp = sj.towns(name, 1);
                        Long sjId = sjResp.objects().stream()
                                .filter(t -> t.title().equalsIgnoreCase(name))
                                .map(SjTownResponse.Item::id)
                                .findFirst()
                                .orElse(null);

                        // сохраняем в кеш
                        Location loc = new Location(
                                hhId,    // hhId
                                sjId,    // sjId
                                null,    // avitoId пока неизвестен
                                name
                        );
                        repo.save(loc);
                        return loc;
                    }
                }
            } catch (Exception e) {
                log.warn("Не удалось подгрузить локацию для '{}': {}", name, e.getMessage());
            }
        }

        // c) как запасной вариант — только SJ
        try {
            SjTownResponse sjResp = sj.towns(name, 1);
            for (var t : sjResp.objects()) {
                if (t.title().equalsIgnoreCase(name)) {
                    Location loc = new Location(
                            null,       // no hhId
                            t.id(),     // sjId
                            null,       // no avitoId
                            name
                    );
                    repo.save(loc);
                    return loc;
                }
            }
        } catch (Exception e) {
            log.warn("Не удалось подгрузить sj-локацию для '{}': {}", name, e.getMessage());
        }

        // ни один API не вернул — оставляем пустым
        return null;
    }

    private Optional<Long> tryParseLong(String s) {
        try {
            return Optional.of(Long.parseLong(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
