package ru.zubcov.tenderalertbot.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

@FunctionalInterface
public interface CommandHandler {
    void handle(Long chatId, Update update);
}