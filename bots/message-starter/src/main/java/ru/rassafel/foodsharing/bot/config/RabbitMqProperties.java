package ru.rassafel.foodsharing.bot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqProperties {
    private String exchangeName = "default-application-exchange";

    private String queueName = "default-application-queue";
}
