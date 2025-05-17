package com.thepet.repositories;

import com.thepet.model.Note;
import com.thepet.model.Pet;
import com.thepet.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private Pet savedPet;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(
                User.builder()
                        .email("user@test.com")
                        .password("password123")
                        .name("Test User")
                        .build()
        );

        savedPet = petRepository.save(
                Pet.builder()
                        .name("Fluffy")
                        .species("Cat")
                        .birthDate(LocalDate.now())
                        .owner(user)
                        .build()
        );
    }

    @Test
    void shouldFindByPetId() {
        // Given
        Note note = Note.builder()
                .type("Vaccination")
                .time(LocalDateTime.now())
                .body("Annual vaccination")
                .pet(savedPet)
                .build();
        noteRepository.save(note);

        // When
        List<Note> notes = noteRepository.findByPetId(savedPet.getId());

        // Then
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getType()).isEqualTo("Vaccination");
    }
}