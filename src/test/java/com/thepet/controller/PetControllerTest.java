package com.thepet.controller;

import com.thepet.model.Pet;
import com.thepet.service.PetService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    @Test
    void createPet_ShouldReturnCreatedPet() {
        Pet pet = new Pet();
        when(petService.createPet(any(), anyLong())).thenReturn(pet);


        ResponseEntity<Pet> response = petController.createPet(pet, 1L);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pet, response.getBody());
        verify(petService).createPet(pet, 1L);
    }

    @Test
    void getPetsByOwner_ShouldReturnPetList() {

        List<Pet> pets = Collections.singletonList(new Pet());
        when(petService.getPetsByOwner(anyLong())).thenReturn(pets);

        ResponseEntity<List<Pet>> response = petController.getPetsByOwner(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(petService).getPetsByOwner(1L);
    }

    @Test
    void getPetById_ShouldReturnPet() {

        Pet pet = new Pet();
        when(petService.getPetById(anyLong())).thenReturn(pet);


        ResponseEntity<Pet> response = petController.getPetById(1L);


        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pet, response.getBody());
        verify(petService).getPetById(1L);
    }

    @Test
    void deletePet_ShouldCallService() {

        petController.deletePet(1L);


        verify(petService).deletePet(1L);
        assertDoesNotThrow(() -> petController.deletePet(1L));
    }

    @Test
    void getPetsByOwner_EmptyList_ShouldReturnEmpty() {

        when(petService.getPetsByOwner(anyLong())).thenReturn(Collections.emptyList());


        ResponseEntity<List<Pet>> response = petController.getPetsByOwner(1L);

        assertTrue(response.getBody().isEmpty());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getPetById_NotFound_ShouldThrow() {

        when(petService.getPetById(anyLong()))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Not found"));

        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> petController.getPetById(1L));
    }
}