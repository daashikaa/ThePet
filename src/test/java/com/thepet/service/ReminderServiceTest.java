package com.thepet.service;

import com.thepet.model.Reminder;
import com.thepet.repositories.ReminderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {
    @Mock
    private ReminderRepository reminderRepository;
    @InjectMocks
    private ReminderService reminderService;

    @Test
    void getRemindersByPet_EmptyList() {
        when(reminderRepository.findByPetId(1L)).thenReturn(Collections.emptyList());
        List<Reminder> reminders = reminderService.getRemindersByPet(1L);
        assertTrue(reminders.isEmpty());
    }

    @Test
    void addReminder_Success() {
        Reminder reminder = new Reminder();
        when(reminderRepository.save(reminder)).thenReturn(reminder);
        Reminder result = reminderService.addReminder(reminder);
        assertEquals(reminder, result);
    }
}