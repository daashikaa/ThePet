package com.thepet.service;

import com.thepet.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    private NoteService noteService;

    @BeforeEach
    void setUp() {
        noteService = new NoteService();
    }

    @Test
    void testListNotes_ShouldReturnInitialNotes() {
        List<Note> notes = noteService.listNotes();

        assertNotNull(notes);
        assertEquals(3, notes.size());
    }

    @Test
    void testAddNote_ShouldIncreaseListSize() {
        Note newNote = new Note(null, 4L, "прививка", LocalDate.of(2025, 5, 5), "Запись о прививке");

        noteService.addNote(newNote);
        List<Note> notes = noteService.listNotes();

        assertEquals(4, notes.size());
        assertTrue(notes.stream().anyMatch(note -> "прививка".equals(note.getType())));
    }

    @Test
    void testDeleteNote_ShouldRemoveNote() {
        Long idToDelete = 2L;

        noteService.deleteNote(idToDelete);
        List<Note> notes = noteService.listNotes();

        assertEquals(2, notes.size());
        assertFalse(notes.stream().anyMatch(note -> note.getId().equals(idToDelete)));
    }

    @Test
    void testDeleteNote_WithInvalidId_ShouldNotChangeList() {
        Long invalidId = 99L;

        noteService.deleteNote(invalidId);
        List<Note> notes = noteService.listNotes();

        assertEquals(3, notes.size());
    }

    @Test
    void testAddNote_UsingMockito() {
        NoteService mockService = mock(NoteService.class);
        Note newNote = new Note(null, 5L, "игрушка", LocalDate.now(), "Купили новую игрушку");

        mockService.addNote(newNote);
        verify(mockService, times(1)).addNote(newNote);
    }

    @Test
    void testDeleteNote_UsingMockito() {
        NoteService mockService = mock(NoteService.class);
        Long idToDelete = 1L;

        mockService.deleteNote(idToDelete);
        verify(mockService, times(1)).deleteNote(idToDelete);
    }
}
