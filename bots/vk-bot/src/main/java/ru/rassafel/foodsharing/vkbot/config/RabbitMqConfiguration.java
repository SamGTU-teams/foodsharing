package ru.rassafel.foodsharing.vkbot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfiguration {
    @Bean
    MessageConverter jsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    FanoutExchange readyExchange(RabbitMqProperties properties) {
        return ExchangeBuilder.fanoutExchange(properties.getReadyPost().getExchange())
            .build();
    }

    @Bean
    Queue readyStorageQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getReadyPost().getStorage())
            .build();
    }

    @Bean
    Binding readyStorageBinding(RabbitMqProperties properties) {
        return BindingBuilder.bind(readyStorageQueue(properties))
            .to(readyExchange(properties));
    }

    @Bean
    Queue readyQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getReadyPost().getQueue())
            .build();
    }

    @Bean
    Binding readyBinding(RabbitMqProperties properties) {
        return BindingBuilder.bind(readyQueue(properties))
            .to(readyExchange(properties));
    }

    @Bean
    FanoutExchange adExchange(RabbitMqProperties properties) {
        return ExchangeBuilder.fanoutExchange(properties.getAdPost().getExchange())
            .build();
    }

    @Bean
    Queue adStorageQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getAdPost().getStorage())
            .build();
    }

    @Bean
    Binding adStorageBinding(RabbitMqProperties properties) {
        return BindingBuilder.bind(adStorageQueue(properties))
            .to(adExchange(properties));
    }

    @Bean
    Queue adQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getAdPost().getQueue())
            .build();
    }

    @Bean
    Binding adBinding(RabbitMqProperties properties) {
        return BindingBuilder.bind(adQueue(properties))
            .to(adExchange(properties));
    }
}
