package com.thepet.controller;

import com.thepet.model.Note;
import com.thepet.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@Tag(name = "Заметки", description = "Управление заметками о питомцах")
@Slf4j
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
        log.info("NoteController initialized");
    }

    @GetMapping("/pet/{petId}")
    @Operation(summary = "Получить заметки по ID питомца")
    public ResponseEntity<List<Note>> getNotesByPet(@PathVariable Long petId) {
        log.info("Getting notes for pet with ID: {}", petId);
        List<Note> notes = noteService.getNotesByPet(petId);
        log.debug("Found {} notes for pet ID: {}", notes.size(), petId);
        return ResponseEntity.ok(notes);
    }

    @PostMapping("/pet/{petId}")
    @Operation(summary = "Добавить заметку для питомца")
    public ResponseEntity<Note> addNote(@Valid @RequestBody Note note, @PathVariable Long petId) {
        log.info("Adding new note for pet ID: {}", petId);
        Note createdNote = noteService.addNote(note, petId);
        log.info("Note created with ID: {}", createdNote.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "Удалить заметку")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        log.info("Deleting note with ID: {}", noteId);
        noteService.deleteNote(noteId);
        log.info("Note with ID {} deleted successfully", noteId);
        return ResponseEntity.noContent().build();
    }
}