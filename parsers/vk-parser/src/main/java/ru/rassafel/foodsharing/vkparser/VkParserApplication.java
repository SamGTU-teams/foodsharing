package ru.rassafel.foodsharing.vkparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VkParserApplication {
    public static void main(String[] args) {
        SpringApplication.run(VkParserApplication.class, args);
    }
}
