package com.thepet.controller;

import com.thepet.model.Reminder;
import com.thepet.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
@Tag(name = "Напоминания", description = "Управление напоминаниями для питомцев")
public class ReminderController {
    private final ReminderService reminderService;

    @GetMapping("/pet/{petId}")
    @Operation(summary = "Получить напоминания по ID питомца")
    public ResponseEntity<List<Reminder>> getRemindersByPet(@PathVariable Long petId) {
        return ResponseEntity.ok(reminderService.getRemindersByPet(petId));
    }

    @PostMapping
    @Operation(summary = "Создать напоминание")
    public ResponseEntity<Reminder> createReminder(@Valid @RequestBody Reminder reminder) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reminderService.addReminder(reminder));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить напоминание")
    public ResponseEntity<Reminder> updateReminder(@PathVariable Long id, @Valid @RequestBody Reminder reminder) {
        reminder.setId(id);
        return ResponseEntity.ok(reminderService.updateReminder(reminder));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить напоминание")

    public ResponseEntity<Void> deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }
}