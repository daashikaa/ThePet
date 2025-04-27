package com.thepet.controller;

import com.thepet.model.Pet;
import com.thepet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@Tag(name = "Питомцы", description = "Управление питомцами")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/{ownerId}")
    @Operation(summary = "Создать питомца для пользователя")
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet pet, @PathVariable Long ownerId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.createPet(pet, ownerId));
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Получить питомцев по владельцу")
    public ResponseEntity<List<Pet>> getPetsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(petService.getPetsByOwner(ownerId));
    }

    @GetMapping("/{petId}")
    @Operation(summary = "Получить питомца по ID")
    public ResponseEntity<Pet> getPetById(@PathVariable Long petId) {
        return ResponseEntity.ok(petService.getPetById(petId));
    }

    @DeleteMapping("/{petId}")
    @Operation(summary = "Удалить питомца")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }
}