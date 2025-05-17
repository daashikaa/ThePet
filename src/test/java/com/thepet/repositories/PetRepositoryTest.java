package com.thepet.repositories;

import com.thepet.model.Pet;
import com.thepet.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(
                User.builder()
                        .email("owner@test.com")
                        .password("password") // Исправлено с "pass" на "password"
                        .name("Owner")
                        .build()
        );
    }

    @Test
    void shouldFindByOwnerId() {
        // Given
        Pet pet = Pet.builder()
                .name("Buddy")
                .species("Dog")
                .birthDate(LocalDate.now()) // Добавьте эту строку
                .owner(savedUser)
                .build();
        petRepository.save(pet);

        // When
        List<Pet> pets = petRepository.findByOwnerId(savedUser.getId());

        // Then
        assertThat(pets).hasSize(1);
        assertThat(pets.get(0).getName()).isEqualTo("Buddy");
    }
}