package com.thepet.service;

import com.thepet.exception.ResourceNotFoundException;
import com.thepet.model.Reminder;
import com.thepet.repositories.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final NotificationService notificationService;

    public List<Reminder> getRemindersByPet(Long petId) {
        log.info("Getting reminders for pet ID: {}", petId);
        return reminderRepository.findByPetId(petId);
    }

    public Reminder addReminder(Reminder reminder) {
        log.info("Adding new reminder");
        Reminder savedReminder = reminderRepository.save(reminder);
        log.info("Added reminder with ID: {}", savedReminder.getId());
        return savedReminder;
    }

    public void deleteReminder(Long reminderId) {
        log.info("Deleting reminder ID: {}", reminderId);
        if (!reminderRepository.existsById(reminderId)) {
            log.error("Reminder not found with ID: {}", reminderId);
            throw new ResourceNotFoundException("Reminder not found with id: " + reminderId);
        }
        reminderRepository.deleteById(reminderId);
        log.info("Deleted reminder ID: {}", reminderId);
    }

    public Reminder updateReminder(Reminder reminder) {
        log.info("Updating reminder ID: {}", reminder.getId());
        if (!reminderRepository.existsById(reminder.getId())) {
            log.error("Reminder not found with ID: {}", reminder.getId());
            throw new ResourceNotFoundException("Reminder not found with id: " + reminder.getId());
        }
        Reminder updatedReminder = reminderRepository.save(reminder);
        log.info("Updated reminder ID: {}", updatedReminder.getId());
        return updatedReminder;
    }

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        log.info("Checking for due reminders");
        List<Reminder> reminders = reminderRepository.findByCountedDateBefore(LocalDateTime.now());
        log.info("Found {} reminders to process", reminders.size());
        reminders.forEach(reminder -> {
            notificationService.sendReminder(reminder);
            reminderRepository.delete(reminder);
        });
    }
}