package com.thepet.service;

import com.thepet.model.Role;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void generateAndParseToken_Success() {
        String token = jwtService.generateToken("test@example.com", Role.USER);
        Claims claims = jwtService.parseToken(token);

        assertEquals("test@example.com", claims.getSubject());
        assertEquals("USER", claims.get("role"));
    }
}