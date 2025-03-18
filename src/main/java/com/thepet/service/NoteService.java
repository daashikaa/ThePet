package com.thepet.service;

import com.thepet.model.Note;
import com.thepet.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;

//    public List<Note> listNotes() {
//        return notes;
//    }
//
//    public void addNote(Note note) {
//        note.setId(++id);
//        notes.add(note);
//    }
//
//    public void deleteNote(Long id) {
//        notes.removeIf(note -> note.getId().equals(id));
//    }
}
