package com.example.vacancy_aggregator.location.service.impl;

import com.example.vacancy_aggregator.location.data.Location;
import com.example.vacancy_aggregator.location.service.LocationRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

/**
 * Реализация {@link LocationRepository} на основе in-memory кэша Caffeine.
 * Кэш хранит до 2 дней:
 */
@Repository
public class InMemoryLocationRepository implements LocationRepository {

    private final Cache<String, Location> byHh =
            Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(2)).build();
    private final Cache<Long, Location> bySj =
            Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(2)).build();
    private final Cache<String, Location> byName =
            Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(2)).build();

    private final Cache<Integer, Location> byAvitoId =
            Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(2)).build();

    @Override
    public Optional<Location> byHhId(String hh) {
        return Optional.ofNullable(byHh.getIfPresent(hh));
    }

    @Override
    public Optional<Location> bySjId(Long sj) {
        return Optional.ofNullable(bySj.getIfPresent(sj));
    }

    public Optional<Location> byAvitoId(Integer avitoId) {
        return Optional.ofNullable(byAvitoId.getIfPresent(avitoId));
    }

    @Override
    public Optional<Location> byName(String n) {
        return Optional.ofNullable(byName.getIfPresent(n.toLowerCase()));
    }

    @Override
    public void save(Location loc) {
        if (loc.hhId() != null) byHh.put(loc.hhId(), loc);
        if (loc.sjId() != null) bySj.put(loc.sjId(), loc);
        if (loc.avitoId() != null) byAvitoId.put(loc.avitoId(), loc);
        byName.put(loc.name().toLowerCase(), loc);
    }
}
