package com.thepet.service;

import com.thepet.model.Pet;
import com.thepet.model.User;
import com.thepet.repositories.PetRepository;
import com.thepet.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    @Mock
    private PetRepository petRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PetService petService;

    @Test
    void createPet_UserNotFound_ThrowsException() {
        Pet pet = new Pet();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> petService.createPet(pet, 1L));
    }

    @Test
    void createPet_Success() {
        User user = new User();
        Pet pet = new Pet();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(petRepository.save(pet)).thenReturn(pet);
        Pet result = petService.createPet(pet, 1L);
        assertEquals(user, result.getOwner());
    }

    @Test
    void getPetsByOwner_EmptyList() {
        when(petRepository.findByOwnerId(1L)).thenReturn(Collections.emptyList());
        List<Pet> pets = petService.getPetsByOwner(1L);
        assertTrue(pets.isEmpty());
    }
}