package com.example.vacancy_aggregator.favorite.vacancy.web;

import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.data.vacancy.Vacancy;
import com.example.vacancy_aggregator.favorite.vacancy.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для работы с избранными (лайкнутыми) вакансиями текущего пользователя.
 */
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService service;
    private final UserRepository users;


    /**
     * Добавляет вакансию в список избранных для текущего пользователя.
     *
     * @param ud аутентифицированный пользователь
     * @param v  вакансия, которую пользователь лайкает
     */
    @PostMapping
    public void like(@AuthenticationPrincipal UserDetails ud,
                     @RequestBody Vacancy v) {
        service.like(users.findByEmail(ud.getUsername()).orElseThrow(), v);
    }

    /**
     * Убирает вакансию из списка избранных для текущего пользователя.
     *
     * @param ud         аутентифицированный пользователь
     * @param source     источник вакансии ("hh", "sj" или "avito")
     * @param externalId внешний идентификатор вакансии в этом источнике
     */
    @DeleteMapping("/{source}/{externalId}")
    public void unlike(@AuthenticationPrincipal UserDetails ud,
                       @PathVariable String source,
                       @PathVariable String externalId) {
        service.unlike(users.findByEmail(ud.getUsername()).orElseThrow(), source, externalId);
    }

    /**
     * Возвращает список всех избранных вакансий текущего пользователя.
     *
     * @param ud аутентифицированный пользователь
     * @return список {@link Vacancy}
     */
    @GetMapping
    public List<Vacancy> list(@AuthenticationPrincipal UserDetails ud) {
        return service.list(users.findByEmail(ud.getUsername()).orElseThrow());
    }
}