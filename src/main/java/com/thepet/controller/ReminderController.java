package com.thepet.controller;

import com.thepet.model.Reminder;
import com.thepet.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
@Tag(name = "Напоминания", description = "Управление напоминаниями для питомцев")
@Slf4j
public class ReminderController {
    private final ReminderService reminderService;

    @GetMapping("/pet/{petId}")
    @Operation(summary = "Получить напоминания по ID питомца")
    public ResponseEntity<List<Reminder>> getRemindersByPet(@PathVariable Long petId) {
        log.info("Getting reminders for pet ID: {}", petId);
        return ResponseEntity.ok(reminderService.getRemindersByPet(petId));
    }

    @PostMapping
    @Operation(summary = "Создать напоминание")
    public ResponseEntity<Reminder> createReminder(@Valid @RequestBody Reminder reminder) {
        log.info("Creating new reminder");
        Reminder createdReminder = reminderService.addReminder(reminder);
        log.info("Reminder created with ID: {}", createdReminder.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReminder);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить напоминание")
    public ResponseEntity<Reminder> updateReminder(@PathVariable Long id, @Valid @RequestBody Reminder reminder) {
        log.info("Updating reminder ID: {}", id);
        reminder.setId(id);
        return ResponseEntity.ok(reminderService.updateReminder(reminder));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить напоминание")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long id) {
        log.info("Deleting reminder ID: {}", id);
        reminderService.deleteReminder(id);
        log.info("Reminder deleted ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}