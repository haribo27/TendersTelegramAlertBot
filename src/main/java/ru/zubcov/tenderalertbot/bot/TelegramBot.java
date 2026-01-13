package ru.zubcov.tenderalertbot.bot;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zubcov.tenderalertbot.bot.handler.ChatCommandService;
import ru.zubcov.tenderalertbot.bot.handler.CommandHandler;
import ru.zubcov.tenderalertbot.user.User;
import ru.zubcov.tenderalertbot.user.UserService;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatCommandService chatCommandService;
    @Autowired
    private TelegramService telegramService;

    private final Map<String, CommandHandler> handlers = new HashMap<>();

    @PostConstruct
    public void init() {
        telegramService.setSender(this);
    }

    public TelegramBot() {
        super(BotConfig.getBotToken());
    }

    @Override
    public String getBotUsername() {
        return BotConfig.getBotUsername();
    }

    @PostConstruct
    private void initHandlers() {

        handlers.put("/start", chatCommandService::handleStart);
        handlers.put("🔔 Подписаться", chatCommandService::handleSubscribe);
        handlers.put("🔕 Отписаться", chatCommandService::handleUnsubscribe);
        // handlers.put("⚙️ Мои настройки", chatCommandService::handleSettings);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            User user = userService.registerIfNotExists(update.getMessage().getFrom());

            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            CommandHandler handler = handlers.get(text);

            if (handler != null) {
                handler.handle(chatId, update);
            } else {
                chatCommandService.handleUnknown(chatId);
            }
        }
    }
}
