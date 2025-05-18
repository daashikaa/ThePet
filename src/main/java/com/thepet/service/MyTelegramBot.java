package com.thepet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        log.debug("Getting bot username");
        return "the_pet_java_bot";
    }

    @Override
    public String getBotToken() {
        log.debug("Getting bot token");
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            log.info("Received message from chat ID: {}", chatId);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText("Привет! Я бот ThePet. Напоминания и статистика будут приходить сюда.");

            try {
                execute(response);
                log.debug("Response sent to chat ID: {}", chatId);
            } catch (TelegramApiException e) {
                log.error("Error sending message to chat ID: {}", chatId, e);
            }
        }
    }
}