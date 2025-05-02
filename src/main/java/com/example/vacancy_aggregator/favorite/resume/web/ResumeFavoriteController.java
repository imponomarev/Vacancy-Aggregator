package com.example.vacancy_aggregator.favorite.resume.web;

import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.favorite.resume.service.ResumeFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resume-favorites")
@RequiredArgsConstructor
public class ResumeFavoriteController {

    private final ResumeFavoriteService service;
    private final UserRepository users;

    /** Добавить лайк */
    @PostMapping
    public void like(@AuthenticationPrincipal UserDetails ud,
                     @RequestBody Resume r) {
        service.like(users.findByEmail(ud.getUsername()).orElseThrow(), r);
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
    public List<Resume> list(@AuthenticationPrincipal UserDetails ud) {
        return service.list(users.findByEmail(ud.getUsername()).orElseThrow());
    }
}
