package ru.zubcov.tenderalertbot.bot;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Optional;

public class BotConfig {
    private static final Dotenv dotenv;

    static {
        dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }

    private static final String BOT_TOKEN = Optional.ofNullable(System.getenv("TELEGRAM_BOT_TOKEN"))
            .orElseGet(() -> Optional.ofNullable(dotenv.get("TELEGRAM_BOT_TOKEN"))
                    .orElseThrow(() -> new IllegalStateException("TELEGRAM_BOT_TOKEN is not set in environment or .env file")));

    private static final String BOT_USERNAME = Optional.ofNullable(System.getenv("TELEGRAM_BOT_USERNAME"))
            .orElseGet(() -> Optional.ofNullable(dotenv.get("TELEGRAM_BOT_USERNAME"))
                    .orElseThrow(() -> new IllegalStateException("TELEGRAM_BOT_USERNAME is not set in environment or .env file")));

    public static String getBotToken() {
        return BOT_TOKEN;
    }

    public static String getBotUsername() {
        return BOT_USERNAME;
    }
}