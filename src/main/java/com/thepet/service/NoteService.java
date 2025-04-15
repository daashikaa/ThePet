package com.thepet.service;

import com.thepet.exception.ResourceNotFoundException;
import com.thepet.model.Note;
import com.thepet.model.Pet;
import com.thepet.repositories.NoteRepository;
import com.thepet.repositories.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;
    private final PetRepository petRepository;

    public List<Note> getNotesByPet(Long petId) {
        return noteRepository.findByPetId(petId);
    }

    public Note addNote(Note note, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + petId));
        note.setPet(pet);
        return noteRepository.save(note);
    }

    public void deleteNote(Long noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new ResourceNotFoundException("Note not found with id: " + noteId);
        }
        noteRepository.deleteById(noteId);
    }

    public Note updateNote(Note note) {
        if (!noteRepository.existsById(note.getId())) {
            throw new ResourceNotFoundException("Note not found with id: " + note.getId());
        }
        return noteRepository.save(note);
    }
}