package ru.rassafel.foodsharing.vkparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VkParserApplication {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        SpringApplication.run(VkParserApplication.class, args);
    }
}
