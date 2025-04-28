package com.example.vacancy_aggregator.favorite.web;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.data.Vacancy;
import com.example.vacancy_aggregator.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService service;
    private final UserRepository users;


    /** Добавить лайк */
    @PostMapping
    public void like(@AuthenticationPrincipal UserDetails ud,
                     @RequestBody Vacancy v) {
        service.like(users.findByEmail(ud.getUsername()).orElseThrow(), v);
    }

    /** Убрать лайк */
    @DeleteMapping("/{source}/{externalId}")
    public void unlike(@AuthenticationPrincipal UserDetails ud,
                       @PathVariable String source,
                       @PathVariable String externalId) {
        service.unlike(users.findByEmail(ud.getUsername()).orElseThrow(), source, externalId);
    }

    /** Список лайков */
    @GetMapping
    public List<Vacancy> list(@AuthenticationPrincipal UserDetails ud) {
        return service.list(users.findByEmail(ud.getUsername()).orElseThrow());
    }
}