package ru.rassafel.foodsharing.tgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TgBotApplication {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
    }
}
