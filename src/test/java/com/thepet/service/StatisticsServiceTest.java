package com.thepet.service;

import com.thepet.model.Note;
import com.thepet.model.Pet;
import com.thepet.repositories.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void getWeeklyStats_shouldReturnCorrectCounts() {
        // Arrange
        Pet testPet = new Pet();
        testPet.setId(1L);

        List<Note> testNotes = Arrays.asList(
                createNote("health", LocalDateTime.now().minusDays(3), testPet),
                createNote("nutrition", LocalDateTime.now().minusDays(1), testPet),
                createNote("health", LocalDateTime.now().minusDays(2), testPet)
        );

        when(noteRepository.findByPetIdAndTimeAfter(anyLong(), any()))
                .thenReturn(testNotes);

        // Act
        Map<String, Long> stats = statisticsService.getWeeklyStats(1L);

        // Assert
        assertEquals(2, stats.get("health"));
        assertEquals(1, stats.get("nutrition"));
        assertNull(stats.get("training"));
    }

    @Test
    void getWeeklyStats_shouldReturnEmptyMapWhenNoNotes() {
        when(noteRepository.findByPetIdAndTimeAfter(anyLong(), any()))
                .thenReturn(Collections.emptyList());

        Map<String, Long> stats = statisticsService.getWeeklyStats(1L);

        assertTrue(stats.isEmpty());
    }

    private Note createNote(String type, LocalDateTime time, Pet pet) {
        Note note = new Note();
        note.setType(type);
        note.setTime(time);
        note.setPet(pet);
        return note;
    }
}