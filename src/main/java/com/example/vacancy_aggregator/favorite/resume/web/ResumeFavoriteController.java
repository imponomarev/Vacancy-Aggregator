package com.example.vacancy_aggregator.favorite.resume.web;

import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.favorite.resume.service.ResumeFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления избранными резюме (Resume Favorites).
 * Позволяет PRO-пользователям добавлять, удалять и просматривать
 * свои «лайкнутые» резюме.
 */
@RestController
@RequestMapping("/resume-favorites")
@RequiredArgsConstructor
public class ResumeFavoriteController {

    private final ResumeFavoriteService service;
    private final UserRepository users;

    /**
     * Добавляет резюме в избранное текущего пользователя.
     *
     * @param ud данные аутентифицированного пользователя
     * @param r  объект резюме для «лайка»
     */
    @PostMapping
    public void like(@AuthenticationPrincipal UserDetails ud,
                     @RequestBody Resume r) {
        service.like(users.findByEmail(ud.getUsername()).orElseThrow(), r);
    }

    /**
     * Убирает резюме из избранного текущего пользователя.
     *
     * @param ud          данные аутентифицированного пользователя
     * @param source      код провайдера резюме ("hh" | "sj" | "avito")
     * @param externalId  внешний идентификатор резюме
     */
    @DeleteMapping("/{source}/{externalId}")
    public void unlike(@AuthenticationPrincipal UserDetails ud,
                       @PathVariable String source,
                       @PathVariable String externalId) {
        service.unlike(users.findByEmail(ud.getUsername()).orElseThrow(), source, externalId);
    }

    /**
     * Возвращает список всех избранных резюме текущего пользователя.
     *
     * @param ud данные аутентифицированного пользователя
     * @return список DTO резюме
     */
    @GetMapping
    public List<Resume> list(@AuthenticationPrincipal UserDetails ud) {
        return service.list(users.findByEmail(ud.getUsername()).orElseThrow());
    }
}
