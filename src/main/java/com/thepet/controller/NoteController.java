package com.thepet.controller;

import com.thepet.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;


    @GetMapping("/notes")
    public String notes(Model model) {
        model.addAttribute("notes", noteService.listNotes());
        return "notes";
    }

}
