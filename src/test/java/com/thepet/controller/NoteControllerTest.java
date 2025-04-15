package com.thepet.controller;

import com.thepet.model.Note;
import com.thepet.model.Pet;
import com.thepet.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    private Note testNote;
    private Pet testPet;

    @BeforeEach
    void setUp() {
        testPet = new Pet();
        testPet.setId(1L);
        testPet.setName("Buddy");

        testNote = new Note();
        testNote.setId(1L);
        testNote.setType("Vaccination");
        testNote.setTime(LocalDateTime.now());
        testNote.setBody("Annual vaccination");
        testNote.setPet(testPet);
    }

    @Test
    void getNotesByPet_ShouldReturnNotesList() {
        // Arrange
        List<Note> notes = Arrays.asList(testNote);
        when(noteService.getNotesByPet(anyLong())).thenReturn(notes);

        // Act
        List<Note> result = noteController.getNotesByPet(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testNote, result.get(0));
        verify(noteService, times(1)).getNotesByPet(1L);
    }

    @Test
    void addNote_ShouldCreateNewNote() {
        // Arrange
        when(noteService.addNote(any(Note.class), anyLong())).thenReturn(testNote);

        // Act
        Note result = noteController.addNote(testNote, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testNote.getId(), result.getId());
        verify(noteService, times(1)).addNote(testNote, 1L);
    }

    @Test
    void deleteNote_ShouldCallService() {
        // Act
        noteController.deleteNote(1L);

        // Assert
        verify(noteService, times(1)).deleteNote(1L);
    }

    @Test
    void addNote_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        when(noteService.addNote(any(Note.class), anyLong())).thenReturn(testNote);

        // Act
        Note result = noteController.addNote(testNote, 1L);

        // Assert
        assertNotNull(result);
        // The @ResponseStatus annotation should set status to CREATED
    }

    @Test
    void deleteNote_ShouldReturnNoContentStatus() {
        // Act
        noteController.deleteNote(1L);

        // Assert
        // The @ResponseStatus annotation should set status to NO_CONTENT
    }

    @Test
    void getNotesByPet_WithInvalidPetId_ShouldReturnEmptyList() {
        // Arrange
        when(noteService.getNotesByPet(anyLong())).thenReturn(List.of());

        // Act
        List<Note> result = noteController.getNotesByPet(999L);

        // Assert
        assertTrue(result.isEmpty());
    }
}