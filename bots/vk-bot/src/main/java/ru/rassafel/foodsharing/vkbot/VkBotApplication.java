package ru.rassafel.foodsharing.vkbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VkBotApplication {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        SpringApplication.run(VkBotApplication.class, args);
    }
}
