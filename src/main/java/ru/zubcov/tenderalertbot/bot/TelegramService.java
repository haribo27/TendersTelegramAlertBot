package ru.zubcov.tenderalertbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.ref.PhantomReference;

@Service
@Slf4j
public class TelegramService {

    private AbsSender sender;

    public void setSender(AbsSender sender) {
        this.sender = sender;
    }

    public void send(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        try {
            sender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(keyboard)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Telegram send error", e);
        }
    }

    public void send(Long chatId, String text) {
        try {
            sender.execute(SendMessage.builder()
                    .chatId(chatId)
                            .parseMode("HTML")
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Telegram send error", e);
        }
    }
}
