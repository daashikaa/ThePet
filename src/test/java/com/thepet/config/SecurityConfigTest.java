package com.thepet.config;

import com.thepet.model.Role;
import com.thepet.model.User;
import com.thepet.repositories.UserRepository;
import com.thepet.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    private String userToken;
    private String adminToken;

    @BeforeEach
    void setup() {
        // Очистка данных перед каждым тестом
        userRepository.deleteAll();

        User user = new User();
        user.setName("Test User");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        userRepository.save(user);

        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setPassword("password");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        userToken = jwtService.generateToken("user@example.com", Role.USER);
        adminToken = jwtService.generateToken("admin@example.com", Role.ADMIN);
    }
    @AfterEach
    void tearDown() {
        userRepository.deleteAll(); // Clean up after each test
    }

    @Test
    void accessSecuredEndpointWithoutToken_ShouldFail() throws Exception {
        mockMvc.perform(get("/api/pets/owner/1"))
                .andExpect(status().isUnauthorized()); // Ожидается 401 вместо 403
    }

    @Test
    void accessAdminEndpointWithUserRole_ShouldForbid() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAdminEndpointWithAdminRole_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void invalidJwtToken_ShouldFail() throws Exception {
        mockMvc.perform(get("/api/pets/1")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }
}