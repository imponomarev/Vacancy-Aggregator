package com.example.vacancy_aggregator.location.service;

import com.example.vacancy_aggregator.location.data.Location;

import java.util.Optional;

public interface LocationRepository {
    Optional<Location>    byHhId(String hhId);
    Optional<Location>    bySjId(Long sjId);
    Optional<Location> byAvitoId(Integer avitoId);
    Optional<Location>    byName(String name);
    void                  save(Location loc);
    Iterable<Location>    findAll();
}