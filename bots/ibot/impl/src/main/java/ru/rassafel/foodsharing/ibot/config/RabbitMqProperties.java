package ru.rassafel.foodsharing.ibot.config;

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

    private Declare readyPost;

    @Getter
    @Setter
    public static class Declare {
        private String exchange;
        private String storage;
        private String queue;
        private String dlx;
        private String dlq;
        private String dlqStorage;
    }
}
