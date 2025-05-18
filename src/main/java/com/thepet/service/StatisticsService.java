package com.thepet.service;

import com.thepet.model.Note;
import com.thepet.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final NoteRepository noteRepository;
    private final NotificationService notificationService;

    public Map<String, Long> getWeeklyStats(Long petId) {
        log.info("Getting stats for pet: {}", petId);
        LocalDateTime weekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        return noteRepository.findByPetIdAndTimeAfter(petId, weekAgo)
                .stream()
                .collect(Collectors.groupingBy(Note::getType, Collectors.counting()));
    }

    public String generateStatsReport(Map<String, Long> stats) {
        if (stats.isEmpty()) {
            return "За последнюю неделю не было добавлено ни одной записи.";
        }

        Map.Entry<String, Long> maxEntry = Collections.max(stats.entrySet(), Map.Entry.comparingByValue());
        Map.Entry<String, Long> minEntry = Collections.min(stats.entrySet(), Map.Entry.comparingByValue());

        return String.format(
                "📊 **Статистика за неделю:**\n\n%s\n\n" +
                        "👍 **Молодец!** Больше всего внимания уделено **%s** (%d записей).\n" +
                        "💡 **Совет:** Не забывай про **%s** (всего %d записей).",
                stats.entrySet().stream()
                        .map(e -> String.format("• %s: %d записей", e.getKey(), e.getValue()))
                        .collect(Collectors.joining("\n")),
                maxEntry.getKey(), maxEntry.getValue(),
                minEntry.getKey(), minEntry.getValue()
        );
    }

    @Scheduled(cron = "0 0 12 * * SUN")
    public void sendWeeklyReports() {
        log.info("Starting weekly reports");
        LocalDateTime weekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);

        noteRepository.findPetsWithNotesLastWeek(weekAgo)
                .forEach(pet -> {
                    try {
                        Map<String, Long> stats = getWeeklyStats(pet.getId());
                        String report = generateStatsReport(stats);
                        notificationService.sendStatsReport(pet.getOwner(), report);
                    } catch (Exception e) {
                        log.error("Error sending report for pet: {}", pet.getId(), e);
                    }
                });

        log.info("Weekly reports completed");
    }
}