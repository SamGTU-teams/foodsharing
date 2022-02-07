package ru.rassafel.foodsharing.vkparser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Getter
@Setter
@ConfigurationProperties(prefix = RabbitMqProperties.PREFIX)
public class RabbitMqProperties {
    public static final String PREFIX = "spring.rabbitmq";

    private String exchangeName;
}
