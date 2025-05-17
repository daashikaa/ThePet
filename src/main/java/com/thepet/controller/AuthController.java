package com.thepet.controller;

import com.thepet.model.User;
import com.thepet.model.Role;
import com.thepet.repositories.UserRepository;
import com.thepet.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        log.info("Registering user: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        log.info("Logging in user: {}", user.getEmail());
        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            log.warn("Invalid password for user: {}", user.getEmail());
            throw new RuntimeException("Invalid credentials");
        }
        return jwtService.generateToken(dbUser.getEmail(), dbUser.getRole());
    }
}