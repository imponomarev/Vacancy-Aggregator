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


    /**
     * Универсальный резолвинг с lazy-loading:
     * 1) ищем по hhId,
     * 2) по sjId,
     * 3) по имени,
     * 4) если не нашли — пытаемся достать из API и закешировать.
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

        // 4. Ни в одном не нашли — пробуем получить через API
        Location loc = fetchAndCache(name);
        return Optional.ofNullable(loc);
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
                        Location loc = new Location(hhId, sjId, name);
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
                    Location loc = new Location(null, t.id(), name);
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
