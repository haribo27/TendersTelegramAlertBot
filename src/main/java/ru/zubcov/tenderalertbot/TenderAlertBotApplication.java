package ru.zubcov.tenderalertbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TenderAlertBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenderAlertBotApplication.class, args);
    }

}
