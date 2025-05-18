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
        log.info("Getting notes for pet ID: {}", petId);
        return noteRepository.findByPetId(petId);
    }

    public Note addNote(Note note, Long petId) {
        log.info("Adding note for pet ID: {}", petId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> {
                    log.error("Pet not found with ID: {}", petId);
                    return new EntityNotFoundException("Pet not found with id: " + petId);
                });
        note.setPet(pet);
        Note savedNote = noteRepository.save(note);
        log.info("Note added with ID: {}", savedNote.getId());
        return savedNote;
    }

    public void deleteNote(Long noteId) {
        log.info("Deleting note ID: {}", noteId);
        if (!noteRepository.existsById(noteId)) {
            log.error("Note not found with ID: {}", noteId);
            throw new ResourceNotFoundException("Note not found with id: " + noteId);
        }
        noteRepository.deleteById(noteId);
        log.info("Note deleted ID: {}", noteId);
    }

    public Note updateNote(Note note) {
        log.info("Updating note ID: {}", note.getId());
        if (!noteRepository.existsById(note.getId())) {
            log.error("Note not found with ID: {}", note.getId());
            throw new ResourceNotFoundException("Note not found with id: " + note.getId());
        }
        Note updatedNote = noteRepository.save(note);
        log.info("Note updated ID: {}", updatedNote.getId());
        return updatedNote;
    }
}