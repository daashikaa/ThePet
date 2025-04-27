package com.thepet.filter;

import com.thepet.model.Role;
import com.thepet.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtAuthFilterTest {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private JwtService jwtService;

    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String token = jwtService.generateToken("admin@example.com", Role.ADMIN);
        request.addHeader("Authorization", "Bearer " + token);

        jwtAuthFilter.doFilterInternal(request, response, (req, res) -> {
            assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        });
    }
}