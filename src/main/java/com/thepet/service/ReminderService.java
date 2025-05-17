package com.thepet.service;

import com.thepet.exception.ResourceNotFoundException;
import com.thepet.model.Reminder;
import com.thepet.repositories.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final NotificationService notificationService;

    public List<Reminder> getRemindersByPet(Long petId) {
        return reminderRepository.findByPetId(petId);
    }

    public Reminder addReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(Long reminderId) {
        if (!reminderRepository.existsById(reminderId)) {
            throw new ResourceNotFoundException("Reminder not found with id: " + reminderId);
        }
        reminderRepository.deleteById(reminderId);
    }

    public Reminder updateReminder(Reminder reminder) {
        if (!reminderRepository.existsById(reminder.getId())) {
            throw new ResourceNotFoundException("Reminder not found with id: " + reminder.getId());
        }
        return reminderRepository.save(reminder);
    }

    @Scheduled(fixedRate = 60000) // Проверка каждую минуту
    public void checkReminders() {
        List<Reminder> reminders = reminderRepository.findByCountedDateBefore(LocalDateTime.now());
        reminders.forEach(reminder -> {
            notificationService.sendReminder(reminder);
            reminderRepository.delete(reminder); // Удаляем после отправки
        });
    }
}