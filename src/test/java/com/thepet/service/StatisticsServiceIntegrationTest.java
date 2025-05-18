package com.thepet.service;

import com.thepet.model.Note;
import com.thepet.model.Pet;
import com.thepet.model.User;
import com.thepet.repositories.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceIntegrationTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void sendWeeklyReports_shouldSendNotification() {
        User owner = new User();
        owner.setEmail("test@example.com");

        Pet testPet = new Pet();
        testPet.setId(1L);
        testPet.setOwner(owner);

        List<Note> testNotes = Arrays.asList(
                createNote("health", LocalDateTime.now().minusDays(3), testPet),
                createNote("nutrition", LocalDateTime.now().minusDays(1), testPet)
        );

        when(noteRepository.findPetsWithNotesLastWeek(any()))
                .thenReturn(List.of(testPet));
        when(noteRepository.findByPetIdAndTimeAfter(anyLong(), any()))
                .thenReturn(testNotes);

        statisticsService.sendWeeklyReports();

        verify(notificationService).sendStatsReport(
                eq(owner),
                argThat(message ->
                        message.contains("health: 1 записей") &&
                                message.contains("nutrition: 1 записей") &&
                                message.contains("Больше всего внимания") &&
                                message.contains("Не забывай")
                )
        );
    }

    private Note createNote(String type, LocalDateTime time, Pet pet) {
        Note note = new Note();
        note.setType(type);
        note.setTime(time);
        note.setPet(pet);
        return note;
    }
}