package com.thepet.repositories;

import com.thepet.model.Pet;
import com.thepet.model.Reminder;
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
class ReminderRepositoryTest {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private Pet savedPet;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(
                User.builder()
                        .email("owner@test.com")
                        .password("securepass")
                        .name("Pet Owner")
                        .build()
        );

        savedPet = petRepository.save(
                Pet.builder()
                        .name("Rex")
                        .species("Dog")
                        .birthDate(LocalDate.now())
                        .owner(user)
                        .build()
        );
    }

    @Test
    void shouldFindByPetId() {
        // Given
        Reminder reminder = Reminder.builder()
                .event("Vet Visit")
                .date(LocalDateTime.now().plusDays(7))
                .countedDate(LocalDateTime.now().plusDays(6))
                .pet(savedPet)
                .build();
        reminderRepository.save(reminder);

        // When
        List<Reminder> reminders = reminderRepository.findByPetId(savedPet.getId());

        // Then
        assertThat(reminders).hasSize(1);
        assertThat(reminders.get(0).getEvent()).isEqualTo("Vet Visit");
    }
}