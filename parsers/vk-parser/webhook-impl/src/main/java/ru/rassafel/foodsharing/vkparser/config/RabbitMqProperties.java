package ru.rassafel.foodsharing.vkparser.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = RabbitMqProperties.PREFIX)
public class RabbitMqProperties {
    public static final String PREFIX = "spring.rabbitmq";

    private Declare rawPost;

    @Getter
    @Setter
    public static class Declare {
        private String exchange;
        private String storage;
    }
}
