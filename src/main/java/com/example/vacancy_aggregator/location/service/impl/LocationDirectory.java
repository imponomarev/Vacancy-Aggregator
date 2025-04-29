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
     * Пытается найти или получить заново и закэшировать Location для заданного any:
     * – any может быть hh-ID, sj-ID (число в строке) или человекочитаемым названием.
     */
    public Optional<Location> resolve(String any) {

        if (any == null) {
            return Optional.empty();
        }

        String name = any.trim();

        if (name.isEmpty()) {
            return Optional.empty();
        }

        // 1) Пробуем найти в кеше по hhId, sjId, name или avitoId
        Optional<Location> fromCache = repo.byHhId(name)
                .or(() -> tryParseLong(name).flatMap(repo::bySjId))
                .or(() -> repo.byName(name))
                .or(() -> avito.findRegionId(name).flatMap(repo::byAvitoId));

        if (fromCache.isPresent()) {
            return fromCache;
        }

        // 2) Никого нет в кеше — дергаем все три API
        String hhId = fetchHhId(name);
        Long sjId = fetchSjId(name);
        Integer avitoId = avito.findRegionId(name).orElse(null);

        // 3) Если ни один из API не нашёл ничего — возвращаем пустое
        if (hhId == null && sjId == null && avitoId == null) {
            return Optional.empty();
        }

        // 4) Собираем и сохраняем новую запись
        Location loc = new Location(hhId, sjId, avitoId, name);
        repo.save(loc);
        return Optional.of(loc);
    }

    /**
     * Вызывает HH-Suggest, возвращает первый точный match по text==name или null.
     */
    private String fetchHhId(String name) {
        if (name.length() < 2) {
            return null;
        }
        try {
            SuggestAreaResponse resp = hh.suggest(name, "RU");
            return resp.items().stream()
                    .filter(i -> i.text().equalsIgnoreCase(name))
                    .map(i -> i.id())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.warn("HH-suggest failed for '{}': {}", name, e.getMessage());
            return null;
        }
    }

    /**
     * Вызывает SJ-Towns, возвращает первый точный match по title==name или null.
     */
    private Long fetchSjId(String name) {
        try {
            SjTownResponse resp = sj.towns(name, 1);
            return resp.objects().stream()
                    .filter(t -> t.title().equalsIgnoreCase(name))
                    .map(SjTownResponse.Item::id)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.warn("SJ-towns failed for '{}': {}", name, e.getMessage());
            return null;
        }
    }

    /**
     * Пытается распарсить строку в Long.
     */
    private Optional<Long> tryParseLong(String s) {
        try {
            return Optional.of(Long.parseLong(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
