package com.example.vacancy_aggregator.location.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Сервис для загрузки и поиска ID регионов и городов Avito по названию.
 */
@Service
@Slf4j
public class AvitoLocationService {

    /**
     * URL справочника регионов Авито
     */
    @Value("${avito.locations.url}")
    private String locationsXmlUrl;
    ;

    /**
     * Кэш: название (области или города) → avitoId
     */
    private Map<String, Integer> locationsByName = Collections.emptyMap();

    @PostConstruct
    public void init() {
        try (InputStream in = new URL(locationsXmlUrl).openStream()) {
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(in);

            NodeList regionNodes = doc.getElementsByTagName("Region");
            Map<String, Integer> tmp = new HashMap<>(regionNodes.getLength());

            for (int i = 0; i < regionNodes.getLength(); i++) {
                Element regionEl = (Element) regionNodes.item(i);
                String regionName = regionEl.getAttribute("Name").trim();
                String regionIdStr = regionEl.getAttribute("Id").trim();
                if (!regionName.isEmpty() && !regionIdStr.isEmpty()) {
                    try {
                        Integer regionId = Integer.valueOf(regionIdStr);
                        tmp.put(regionName, regionId);
                    } catch (NumberFormatException nfe) {
                        log.warn("Некорректный Avito Region Id='{}' для '{}'", regionIdStr, regionName);
                    }
                }

                NodeList cityNodes = regionEl.getElementsByTagName("City");
                for (int j = 0; j < cityNodes.getLength(); j++) {
                    Element cityEl = (Element) cityNodes.item(j);
                    String cityName = cityEl.getAttribute("Name").trim();
                    String cityIdStr = cityEl.getAttribute("Id").trim();
                    if (!cityName.isEmpty() && !cityIdStr.isEmpty()) {
                        try {
                            Integer cityId = Integer.valueOf(cityIdStr);
                            tmp.put(cityName, cityId);
                        } catch (NumberFormatException nfe) {
                            log.warn("Некорректный Avito City Id='{}' для '{}'", cityIdStr, cityName);
                        }
                    }
                }
            }

            this.locationsByName = Collections.unmodifiableMap(tmp);
            log.info("Загружено {} записей регионов и городов для Авито", locationsByName.size());

        } catch (Exception ex) {
            log.error("Не удалось загрузить Авито региона из XML: {}", ex.getMessage(), ex);
            this.locationsByName = Collections.emptyMap();
        }
    }

    /**
     * Найти Avito-region-id по точному названию.
     *
     * @param name — точное значение из запроса (например, "Москва")
     */
    public Optional<Integer> findRegionId(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(locationsByName.get(name.trim()));
    }
}