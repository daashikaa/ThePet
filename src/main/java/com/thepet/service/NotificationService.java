package com.thepet.service;

import com.thepet.model.Reminder;
import com.thepet.model.User;
import com.thepet.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    private final MyTelegramBot telegramBot;
    private final UserRepository userRepository;

    public void sendReminder(Reminder reminder) {
        User user = userRepository.findById(reminder.getPet().getOwner().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String message = String.format("Напоминание: %s\nДата: %s",
                reminder.getEvent(), reminder.getCountedDate());

        if (user.getEmail().startsWith("7")) {
            // Отправка в Telegram
            SendMessage telegramMessage = new SendMessage();
            telegramMessage.setChatId(user.getEmail()); // предполагаем, что в email хранится chatId
            telegramMessage.setText(message);
            try {
                telegramBot.execute(telegramMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            // Отправка email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Напоминание от ThePet");
            mailMessage.setText(message);
            mailSender.send(mailMessage);
        }
    }
}