package com.thepet.controller;

import com.thepet.model.Note;
import com.thepet.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@Tag(name = "Заметки", description = "Управление заметками о питомцах")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/pet/{petId}")
    @Operation(summary = "Получить заметки по ID питомца")
    public ResponseEntity<List<Note>> getNotesByPet(@PathVariable Long petId) {
        return ResponseEntity.ok(noteService.getNotesByPet(petId));
    }

    @PostMapping("/pet/{petId}")
    @Operation(summary = "Добавить заметку для питомца")
    public ResponseEntity<Note> addNote(@Valid @RequestBody Note note, @PathVariable Long petId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.addNote(note, petId));
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "Удалить заметку")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build();
    }
}