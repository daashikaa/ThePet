package com.thepet.controller;

import com.thepet.model.User;
import com.thepet.repositories.UserRepository;
import com.thepet.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);

        String result = authController.register(user);
        assertEquals("User registered successfully!", result);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void loginUser_InvalidCredentials_ThrowsException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("wrongPassword");

        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> authController.login(user));
    }
}