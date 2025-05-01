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
 * Сервис для загрузки и поиска ID регионов Avito по названию.
 */
@Service
@Slf4j
public class AvitoLocationService {

    /** URL справочника регионов Авито */
    @Value("${avito.locations.url}")
    private String locationsXmlUrl;
;

    /** Кэш: имя региона → avitoId */
    private Map<String, Integer> regionsByName = Collections.emptyMap();

    @PostConstruct
    public void init() {
        try (InputStream in = new URL(locationsXmlUrl).openStream()) {
            log.info("Loading Avito regions from {}", locationsXmlUrl);
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(in);
            NodeList regionNodes = doc.getElementsByTagName("Region");
            Map<String, Integer> tmp = new HashMap<>(regionNodes.getLength());
            for (int i = 0; i < regionNodes.getLength(); i++) {
                Element regionEl = (Element) regionNodes.item(i);
                String name = regionEl.getAttribute("Name");
                String idStr = regionEl.getAttribute("Id");
                if (!name.isBlank() && !idStr.isBlank()) {
                    try {
                        Integer id = Integer.valueOf(idStr);
                        tmp.put(name.trim(), id);
                    } catch (NumberFormatException nfe) {
                        log.warn("Invalid Avito region Id='{}' for Name='{}'", idStr, name);
                    }
                }
            }
            this.regionsByName = Collections.unmodifiableMap(tmp);
            log.info("Loaded {} Avito regions", regionsByName.size());
        } catch (Exception ex) {
            log.error("Failed to load Avito regions XML: {}", ex.getMessage(), ex);
            this.regionsByName = Collections.emptyMap();
        }
    }

    /**
     * Найти Avito-region-id по точному названию.
     * @param name — точное значение из запроса (например, "Москва")
     */
    public Optional<Integer> findRegionId(String name) {
        if (name == null) {
            return Optional.empty();
        }
        Integer id = regionsByName.get(name.trim());
        return Optional.ofNullable(id);
    }
}