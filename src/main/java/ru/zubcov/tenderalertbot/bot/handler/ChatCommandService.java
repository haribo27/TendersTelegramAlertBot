package ru.zubcov.tenderalertbot.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.zubcov.tenderalertbot.bot.TelegramService;
import ru.zubcov.tenderalertbot.subsciption.SubscriptionService;
import ru.zubcov.tenderalertbot.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatCommandService {

    private final TelegramService telegramService;
    private final SubscriptionService subscriptionService;

    public void handleStart(Long chatId, Update update) {
        telegramService.send(chatId,
                "👋 Привет!\n\n" +
                        "Я отправляю уведомления о новых тендерах по клинингу.\n" +
                        "Выбери действие в меню 👇",
                mainMenu(chatId)
        );
    }

    public void handleSubscribe(Long chatId, Update update) {

        if (subscriptionService.isSubscribed(chatId)) {
            telegramService.send(chatId, "ℹ️ Вы уже подписаны", mainMenu(chatId));
            return;
        }

        subscriptionService.subscribe(chatId);

        telegramService.send(chatId,
                "✅ Вы успешно подписались на уведомления о новых тендерах по клинингу.",
                mainMenu(chatId)
        );
    }

    public void handleUnsubscribe(Long chatId, Update update) {
        subscriptionService.unsubscribe(chatId);
        telegramService.send(chatId, "❌ Вы отписались от уведомлений", mainMenu(chatId));
    }

    public void handleUnknown(Long chatId) {
        telegramService.send(chatId,
                "❓ Я не понял команду.\n" +
                        "Пожалуйста, используйте кнопки меню 👇",
                mainMenu(chatId)
        );
    }

    private ReplyKeyboardMarkup mainMenu(long chatId) {

        boolean isSubscribed = subscriptionService.isSubscribed(chatId);

        List<KeyboardRow> keyboardRows = createKeyboardRows(isSubscribed);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setKeyboard(keyboardRows);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);

        return keyboard;
    }

    private List<KeyboardRow> createKeyboardRows(boolean isSubscribed) {
        List<KeyboardRow> rows = new ArrayList<>();
        if (isSubscribed) {
            KeyboardRow row1 = new KeyboardRow();
            row1.add("🔔 Подписаться");
            row1.add("🔕 Отписаться");

            KeyboardRow row2 = new KeyboardRow();
            row2.add("⚙️ Мои настройки");
            row2.add("❓ Помощь");
            return List.of(row1, row2);
        } else {
            KeyboardRow row1 = new KeyboardRow();
            row1.add("🔔 Подписаться");

            KeyboardRow row2 = new KeyboardRow();
            row2.add("⚙️ Мои настройки");
            row2.add("❓ Помощь");
            return List.of(row1, row2);
        }
    }

}
