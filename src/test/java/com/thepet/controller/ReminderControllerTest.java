package com.thepet.controller;

import com.thepet.model.Reminder;
import com.thepet.service.ReminderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderControllerTest {

    @Mock private ReminderService reminderService;
    @InjectMocks private ReminderController reminderController;

    @Test
    void createReminder_ValidData_ReturnsCreated() {
        Reminder reminder = new Reminder();
        when(reminderService.addReminder(any())).thenReturn(reminder);

        ResponseEntity<Reminder> response = reminderController.createReminder(reminder);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}