package ru.rassafel.foodsharing.analyzer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter jsonMessageConverter,
                                  RabbitMqProperties properties) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(properties.getReadyPost().getExchange());
        return rabbitTemplate;
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
    FanoutExchange rawExchange(RabbitMqProperties properties) {
        return ExchangeBuilder.fanoutExchange(properties.getRawPost().getExchange())
            .build();
    }

    @Bean
    Queue rawQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getRawPost().getQueue())
            .build();
    }

    @Bean
    Binding rawBinding(RabbitMqProperties properties) {
        return BindingBuilder.bind(rawQueue(properties))
            .to(rawExchange(properties));
    }

    @Bean
    Queue rawStorageQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getRawPost().getStorage())
            .build();
    }

    @Bean
    Binding rawStorageBinding(RabbitMqProperties properties) {
        return BindingBuilder.bind(rawStorageQueue(properties))
            .to(rawExchange(properties));
    }
}
