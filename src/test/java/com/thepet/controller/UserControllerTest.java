package com.thepet.controller;

import com.thepet.exception.ResourceNotFoundException;
import com.thepet.model.User;
import com.thepet.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers_ReturnsUsersList() {
        List<User> users = Collections.singletonList(new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        doThrow(ResourceNotFoundException.class).when(userService).deleteUser(anyLong());

        assertThrows(ResourceNotFoundException.class,
                () -> userController.deleteUser(1L));
    }
}