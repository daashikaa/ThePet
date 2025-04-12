package com.thepet.service;

import com.thepet.model.Note;
import com.thepet.model.Pet;
import com.thepet.repositories.NoteRepository;
import com.thepet.repositories.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;
    @Mock
    private PetRepository petRepository;
    @InjectMocks
    private NoteService noteService;

    @Test
    void addNote_PetNotFound_ThrowsException() {
        Note note = new Note();
        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> noteService.addNote(note, 1L));
        verify(noteRepository, never()).save(any());
    }

    @Test
    void addNote_Success() {
        Pet pet = new Pet();
        Note note = new Note();
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));
        when(noteRepository.save(note)).thenReturn(note);
        Note result = noteService.addNote(note, 1L);
        assertNotNull(result);
        assertEquals(pet, result.getPet());
    }
}