package ru.rassafel.foodsharing.vkparser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqProperties {
    private String exchangeName = "default-application-exchange";
}
