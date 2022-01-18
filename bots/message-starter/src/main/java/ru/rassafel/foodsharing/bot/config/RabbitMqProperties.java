package ru.rassafel.foodsharing.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Getter
@Setter
public class RabbitMqProperties {
    private String exchangeName = "default-application-exchange";

    private String queueName = "default-application-queue";
}
