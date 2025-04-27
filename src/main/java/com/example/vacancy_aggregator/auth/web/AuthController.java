package com.example.vacancy_aggregator.auth.web;

import com.example.vacancy_aggregator.auth.entity.User;
import com.example.vacancy_aggregator.auth.repository.UserRepository;
import com.example.vacancy_aggregator.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil util;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String email, @RequestParam String pwd) {

        if (users.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("user exists");
        }

        User u = new User();
        u.setEmail(email);
        u.setPwdHash(encoder.encode(pwd));
        users.save(u);

        return ResponseEntity.ok("created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String pwd) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pwd));
        String jwt = util.generate(email);
        return ResponseEntity.ok(jwt);
    }
}
