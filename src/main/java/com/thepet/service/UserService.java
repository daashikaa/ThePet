package com.thepet.service;

import com.thepet.exception.ResourceNotFoundException;
import com.thepet.model.User;
import com.thepet.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        log.info("Getting user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new EntityNotFoundException("User not found with id: " + userId);
                });
    }

    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        log.info("Saving user");
        User savedUser = userRepository.save(user);
        log.info("User saved with ID: {}", savedUser.getId());
        return savedUser;
    }

    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
        log.info("User deleted with ID: {}", userId);
    }

    public User updateUser(User user) {
        log.info("Updating user with ID: {}", user.getId());
        if (!userRepository.existsById(user.getId())) {
            log.error("User not found with ID: {}", user.getId());
            throw new ResourceNotFoundException("User not found with id: " + user.getId());
        }
        User updatedUser = userRepository.save(user);
        log.info("User updated with ID: {}", updatedUser.getId());
        return updatedUser;
    }
}