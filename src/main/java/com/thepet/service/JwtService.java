package com.thepet.service;

import com.thepet.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    private SecretKey key;

    @PostConstruct
    public void init() {
        log.info("Initializing JWT secret key");
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(String username, Role role) {
        log.info("Generating JWT token for user: {}", username);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name()) // Добавляем роль в токен
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        log.info("Getting authentication from JWT token");
        String user = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return new UsernamePasswordAuthenticationToken(user, null, null);
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
