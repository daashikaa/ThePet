package com.thepet.service;

import com.thepet.exception.ResourceNotFoundException;
import com.thepet.model.Pet;
import com.thepet.model.User;
import com.thepet.repositories.PetRepository;
import com.thepet.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public Pet createPet(Pet pet, Long ownerId) {
        log.info("Creating pet for owner ID: {}", ownerId);
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> {
                    log.error("Owner not found with ID: {}", ownerId);
                    return new EntityNotFoundException("User not found with id: " + ownerId);
                });
        pet.setOwner(owner);
        Pet savedPet = petRepository.save(pet);
        log.info("Created pet with ID: {}", savedPet.getId());
        return savedPet;
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        log.info("Getting pets for owner ID: {}", ownerId);
        return petRepository.findByOwnerId(ownerId);
    }

    public Pet getPetById(Long petId) {
        log.info("Getting pet by ID: {}", petId);
        return petRepository.findById(petId)
                .orElseThrow(() -> {
                    log.error("Pet not found with ID: {}", petId);
                    return new EntityNotFoundException("Pet not found with id: " + petId);
                });
    }

    public void deletePet(Long petId) {
        log.info("Deleting pet with ID: {}", petId);
        if (!petRepository.existsById(petId)) {
            log.error("Pet not found with ID: {}", petId);
            throw new ResourceNotFoundException("Pet not found with id: " + petId);
        }
        petRepository.deleteById(petId);
        log.info("Deleted pet with ID: {}", petId);
    }

    public Pet updatePet(Pet pet) {
        log.info("Updating pet with ID: {}", pet.getId());
        Pet updatedPet = petRepository.save(pet);
        log.info("Updated pet with ID: {}", updatedPet.getId());
        return updatedPet;
    }
}