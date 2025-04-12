package com.thepet.service;

import com.thepet.model.Pet;
import com.thepet.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PetServiceIntegrationTest {

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Test
    void createPet_ShouldLinkOwner() {
        User owner = new User(null, "Alice", "alice@example.com", "pass123", null);
        userService.saveUser(owner);
        Pet pet = new Pet(null, "Buddy", "Dog", "Labrador", LocalDate.now(), owner, null, null);

        Pet savedPet = petService.createPet(pet, owner.getId());

        assertThat(savedPet.getOwner().getId()).isEqualTo(owner.getId());
        assertThat(savedPet.getId()).isNotNull();
    }

    @Test
    void getPetById_ShouldThrowExceptionIfNotFound() {
        assertThrows(EntityNotFoundException.class, () -> petService.getPetById(999L));
    }
}