package com.example.vacancy_aggregator.auth.web;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repo;
    private final PasswordEncoder enc;
    private final JwtUtil jwt;

    @PostMapping("/signup")
    public void signup(@RequestBody AuthDto dto) {
        if (repo.findByEmail(dto.email()).isPresent()) throw new ResponseStatusException(CONFLICT);
        var u = new User();
        u.setEmail(dto.email());
        u.setPwdHash(enc.encode(dto.password()));
        repo.save(u);
    }

    @PostMapping("/signin")
    public TokenDto signin(@RequestBody AuthDto dto) {
        var u = repo.findByEmail(dto.email()).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED));
        if (!enc.matches(dto.password(), u.getPwdHash())) throw new ResponseStatusException(UNAUTHORIZED);
        return new TokenDto(jwt.generate(u));
    }
}

