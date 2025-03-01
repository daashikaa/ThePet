package com.thepet.service;

import com.thepet.model.Note;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    private List<Note> notes = new ArrayList<>();
    private long id = 0;
    {
        notes.add(new Note(++id, 1L, "ветпаспорт", LocalDate.now(), "Запись о ветпаспорте"));
        notes.add(new Note(++id, 2L, "награда", null, "1 место в конкурсе"));
        notes.add(new Note(++id, 3L, "питание", LocalDate.of(2025, 12, 12), "Запись о ветеринарном осмотре"));

    }

    public List<Note> listNotes() { return notes; }

    public void addNote(Note note) {
        note.setId(++id);
        notes.add(note);
    }

    public void deleteNote(Long id) {
        notes.removeIf(note -> note.getId().equals(id));
    }
}
