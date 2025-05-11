package com.example.vacancy_aggregator.location.service;

import com.example.vacancy_aggregator.location.data.Location;

import java.util.Optional;

/**
 * Репозиторий для сохранения и поиска кэшированных данных о локациях
 * (региональных идентификаторах HH, SuperJob и Avito).
 */
public interface LocationRepository {
    /**
     * Поиск локации по HH-region-id.
     *
     * @param hhId строковый идентификатор региона HH
     * @return Optional с найденным {@link Location}
     */
    Optional<Location> byHhId(String hhId);

    /**
     * Поиск локации по SuperJob-id.
     *
     * @param sjId числовой идентификатор региона SuperJob
     * @return Optional с найденным {@link Location}
     */
    Optional<Location> bySjId(Long sjId);

    /**
     * Поиск локации по Avito-region-id.
     *
     * @param avitoId числовой идентификатор региона Avito
     * @return Optional с найденным {@link Location}
     */
    Optional<Location> byAvitoId(Integer avitoId);

    /**
     * Поиск локации по человекочитаемому названию.
     *
     * @param name название региона
     * @return Optional с найденным {@link Location}
     */
    Optional<Location> byName(String name);

    /**
     * Сохранение новой записи о локации в репозиторий.
     *
     * @param loc объект {@link Location} с данными для сохранения
     */
    void save(Location loc);
}