package com.thepet.service;

import com.thepet.model.Reminder;
import com.thepet.model.User;
import com.thepet.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final JavaMailSender mailSender;
    private final MyTelegramBot telegramBot;
    private final UserRepository userRepository;

    public void sendReminder(Reminder reminder) {
        log.info("Sending reminder for pet ID: {}", reminder.getPet().getId());

        User user = userRepository.findById(reminder.getPet().getOwner().getId())
                .orElseThrow(() -> {
                    log.error("User not found for pet ID: {}", reminder.getPet().getId());
                    return new EntityNotFoundException("User not found");
                });

        String message = String.format("Напоминание: %s\nДата: %s",
                reminder.getEvent(), reminder.getCountedDate());

        if (user.getEmail().contains("@")) {
            log.debug("Sending email to: {}", user.getEmail());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Напоминание от ThePet");
            mailMessage.setText(message);
            mailSender.send(mailMessage);
            log.info("Email reminder sent to user ID: {}", user.getId());
        } else {
            log.debug("Sending Telegram message to chat ID: {}", user.getEmail());
            SendMessage telegramMessage = new SendMessage();
            telegramMessage.setChatId(user.getEmail());
            telegramMessage.setText(message);
            try {
                telegramBot.execute(telegramMessage);
                log.info("Telegram reminder sent to user ID: {}", user.getId());
            } catch (TelegramApiException e) {
                log.error("Failed to send Telegram message to user ID: {}", user.getId(), e);
            }
        }
    }

    public void sendStatsReport(User user, String report) {
        log.info("Sending stats report to user ID: {}", user.getId());

        String message = "📅 **Еженедельный отчёт ThePet**\n\n" + report;

        if (user.getEmail().contains("@")) {
            log.debug("Sending email report to: {}", user.getEmail());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("📊 Ваша еженедельная статистика ThePet");
            mailMessage.setText(message);
            mailSender.send(mailMessage);
            log.info("Email report sent to user ID: {}", user.getId());
        } else {
            log.debug("Sending Telegram report to chat ID: {}", user.getEmail());
            SendMessage telegramMessage = new SendMessage();
            telegramMessage.setChatId(user.getEmail());
            telegramMessage.setText(message);
            try {
                telegramBot.execute(telegramMessage);
                log.info("Telegram report sent to user ID: {}", user.getId());
            } catch (TelegramApiException e) {
                log.error("Failed to send Telegram report to user ID: {}", user.getId(), e);
            }
        }
    }
}