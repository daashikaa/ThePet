package com.thepet.service;

import com.thepet.model.Note;
import com.thepet.model.Pet;
import com.thepet.model.User;
import com.thepet.repositories.NoteRepository;
import com.thepet.repositories.PetRepository;
import com.thepet.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoteServiceIntegrationTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void addNoteAndFindByPet() {
        User owner = new User();
        owner.setName("John");
        owner.setEmail("john@test.com");
        owner.setPassword("password");
        userRepository.save(owner);

        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setSpecies("Dog");
        pet.setBreed("Labrador");
        pet.setBirthDate(LocalDate.now());
        pet.setOwner(owner);
        petRepository.save(pet);

        Note note = new Note();
        note.setBody("Vaccination done");
        note.setPet(pet);
        noteRepository.save(note);

        List<Note> notes = noteRepository.findByPetId(pet.getId());
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getBody()).isEqualTo("Vaccination done");
    }
}