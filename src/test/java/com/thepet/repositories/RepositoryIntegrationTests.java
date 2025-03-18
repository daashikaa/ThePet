package com.thepet.repositories;

import com.thepet.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;


import java.time.LocalDate;
import java.time.LocalDateTime;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Test
    void testUserSaveAndRetrieve() {
        User user = new User(null, "Alice", "alice@example.com", "securepass", null);
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Alice");
    }

    @Test
    void testPetSaveAndRetrieve() {
        User owner = userRepository.save(new User(null, "Bob", "bob@example.com", "password", null));
        Pet pet = new Pet(null, "Fluffy", "Dog", "Labrador", LocalDate.of(2020, 5, 15), owner, null, null);
        Pet savedPet = petRepository.save(pet);

        assertThat(savedPet.getId()).isNotNull();
        assertThat(savedPet.getName()).isEqualTo("Fluffy");
    }

    @Test
    void testNoteSaveAndRetrieve() {
        User owner = userRepository.save(new User(null, "Charlie", "charlie@example.com", "pass123", null));
        Pet pet = petRepository.save(new Pet(null, "Whiskers", "Cat", "Siamese", LocalDate.of(2019, 7, 23), owner, null, null));
        Note note = new Note(null, "Health", LocalDateTime.now(), "Regular checkup", pet);
        Note savedNote = noteRepository.save(note);

        assertThat(savedNote.getId()).isNotNull();
        assertThat(savedNote.getBody()).isEqualTo("Regular checkup");
    }

    @Test
    void testReminderSaveAndRetrieve() {
        User owner = userRepository.save(new User(null, "David", "david@example.com", "mypassword", null));
        Pet pet = petRepository.save(new Pet(null, "Bella", "Rabbit", "Angora", LocalDate.of(2021, 3, 10), owner, null, null));
        Reminder reminder = new Reminder(null, "Vet Visit", LocalDateTime.now(), LocalDateTime.now().plusDays(7), pet);
        Reminder savedReminder = reminderRepository.save(reminder);

        assertThat(savedReminder.getId()).isNotNull();
        assertThat(savedReminder.getEvent()).isEqualTo("Vet Visit");
    }
}
