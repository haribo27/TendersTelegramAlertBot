package ru.zubcov.tenderalertbot.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegram.bot")
@Getter
@Setter
public class BotConfig {
    private String token;
    private String username;
}