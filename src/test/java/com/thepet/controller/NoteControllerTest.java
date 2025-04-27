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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
        List<Note> notes = Arrays.asList(testNote);
        when(noteService.getNotesByPet(anyLong())).thenReturn(notes);

        ResponseEntity<List<Note>> response = noteController.getNotesByPet(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testNote, response.getBody().get(0));
        verify(noteService, times(1)).getNotesByPet(1L);
    }

    @Test
    void addNote_ShouldReturnCreatedStatusAndNote() {

        when(noteService.addNote(any(Note.class), anyLong())).thenReturn(testNote);


        ResponseEntity<Note> response = noteController.addNote(testNote, 1L);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testNote.getId(), response.getBody().getId());
        verify(noteService, times(1)).addNote(testNote, 1L);
    }

    @Test
    void deleteNote_ShouldReturnNoContent() {

        ResponseEntity<Void> response = noteController.deleteNote(1L);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(noteService, times(1)).deleteNote(1L);
    }

    @Test
    void getNotesByPet_WithInvalidPetId_ShouldReturnEmptyList() {

        when(noteService.getNotesByPet(anyLong())).thenReturn(Collections.emptyList());


        ResponseEntity<List<Note>> response = noteController.getNotesByPet(999L);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}