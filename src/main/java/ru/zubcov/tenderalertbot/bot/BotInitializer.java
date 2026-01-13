package ru.zubcov.tenderalertbot.bot;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    private final TelegramBot bot;

    public BotInitializer(TelegramBot bot) {
        this.bot = bot;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startBot() throws TelegramApiException {
        TelegramBotsApi botsApi =
                new TelegramBotsApi(DefaultBotSession.class);

        botsApi.registerBot(bot);
    }


}