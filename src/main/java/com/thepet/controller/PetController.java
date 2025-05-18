package com.thepet.controller;

import com.thepet.model.Pet;
import com.thepet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@Tag(name = "Питомцы", description = "Управление питомцами")
@Slf4j
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
        log.info("PetController initialized with PetService");
    }

    @PostMapping("/{ownerId}")
    @Operation(summary = "Создать питомца для пользователя")
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet pet, @PathVariable Long ownerId) {
        log.info("Creating new pet for owner ID: {}", ownerId);
        log.debug("Pet details: name={}", pet.getName());

        Pet createdPet = petService.createPet(pet, ownerId);

        log.info("Successfully created pet with ID: {} for owner ID: {}", createdPet.getId(), ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Получить питомцев по владельцу")
    public ResponseEntity<List<Pet>> getPetsByOwner(@PathVariable Long ownerId) {
        log.info("Getting pets for owner ID: {}", ownerId);

        List<Pet> pets = petService.getPetsByOwner(ownerId);

        log.debug("Found {} pets for owner ID: {}", pets.size(), ownerId);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{petId}")
    @Operation(summary = "Получить питомца по ID")
    public ResponseEntity<Pet> getPetById(@PathVariable Long petId) {
        log.info("Getting pet by ID: {}", petId);

        Pet pet = petService.getPetById(petId);

        if (pet.getOwner() != null) {
            log.debug("Found pet: ID={}, name={}, owner={}",
                    pet.getId(), pet.getName(), pet.getOwner().getId());
        } else {
            log.debug("Found pet: ID={}, name={}, owner=null",
                    pet.getId(), pet.getName());
        }

        return ResponseEntity.ok(pet);
    }

    @DeleteMapping("/{petId}")
    @Operation(summary = "Удалить питомца")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        log.info("Deleting pet with ID: {}", petId);

        petService.deletePet(petId);

        log.info("Successfully deleted pet with ID: {}", petId);
        return ResponseEntity.noContent().build();
    }
}