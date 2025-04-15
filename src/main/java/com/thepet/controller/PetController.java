package com.thepet.controller;

import com.thepet.model.Pet;
import com.thepet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать питомца для пользователя")
    public Pet createPet(@Valid @RequestBody Pet pet, @PathVariable Long ownerId) {
        return petService.createPet(pet, ownerId);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Получить питомцев по владельцу")
    public List<Pet> getPetsByOwner(@PathVariable Long ownerId) {
        return petService.getPetsByOwner(ownerId);
    }

    @GetMapping("/{petId}")
    @Operation(summary = "Получить питомца по ID")
    public Pet getPetById(@PathVariable Long petId) {
        return petService.getPetById(petId);
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить питомца")
    public void deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
    }
}