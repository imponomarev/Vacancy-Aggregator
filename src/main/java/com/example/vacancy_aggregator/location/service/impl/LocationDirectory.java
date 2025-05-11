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

/**
 * Сервис-слой для единой точки доступа к справочнику локаций.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LocationDirectory {

    private final HhSuggestFeign hh;
    private final SjDictFeign sj;
    private final LocationRepository repo;
    private final AvitoLocationService avito;

    /**
     * Разрешает входное значение {@code any} в объект {@link Location}, используя кэш или внешние API.
     *
     * @param any может быть:
     *            HH ID (строка)
     *            SJ ID (число в строковом виде)
     *            Avito ID (строка)
     *            Человекочитаемое название (например, "Москва")
     * @return Optional с найденной или вновь созданной записью {@link Location}, иначе пустой
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
     * Вызывает HH-suggest для поиска региона по точному совпадению текста.
     *
     * @param name входное название региона
     * @return строковый HH ID или null, если не найден
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
     * Вызывает SuperJob API для поиска города по точному совпадению.
     *
     * @param name входное название города
     * @return числовой SJ ID или null, если не найден
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
