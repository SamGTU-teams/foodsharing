package ru.rassafel.foodsharing.tgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TgBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
    }
}
