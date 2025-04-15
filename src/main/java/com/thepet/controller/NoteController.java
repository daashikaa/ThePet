package com.thepet.controller;

import com.thepet.model.Note;
import com.thepet.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public List<Note> getNotesByPet(@PathVariable Long petId) {
        return noteService.getNotesByPet(petId);
    }

    @PostMapping("/pet/{petId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить заметку для питомца")
    public Note addNote(@Valid @RequestBody Note note, @PathVariable Long petId) {
        return noteService.addNote(note, petId);
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить заметку")
    public void deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
    }
}