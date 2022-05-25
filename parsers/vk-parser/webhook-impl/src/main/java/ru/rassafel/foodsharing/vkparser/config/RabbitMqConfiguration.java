package ru.rassafel.foodsharing.vkparser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
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
@EnableRabbit
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfiguration {
    @Bean
    MessageConverter jsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    RabbitTemplate callbackRabbitTemplate(ConnectionFactory connectionFactory,
                                          MessageConverter jsonMessageConverter,
                                          RabbitMqProperties properties) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(properties.getVkParserCallback().getExchange());
        return rabbitTemplate;
    }

    @Bean
    RabbitTemplate rawRabbitTemplate(ConnectionFactory connectionFactory,
                                     MessageConverter jsonMessageConverter,
                                     RabbitMqProperties properties) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(properties.getRawPost().getExchange());
        return rabbitTemplate;
    }

    @Bean
    FanoutExchange vkCallbackExchange(RabbitMqProperties properties) {
        return ExchangeBuilder.fanoutExchange(properties.getVkParserCallback().getExchange())
            .build();
    }

    @Bean
    Queue vkCallbackStorageQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getVkParserCallback().getStorage())
            .build();
    }

    @Bean
    Binding vkCallbackStorageBinding(RabbitMqProperties properties) {
        return BindingBuilder.bind(vkCallbackStorageQueue(properties))
            .to(vkCallbackExchange(properties));
    }

    @Bean
    Queue vkCallbackQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getVkParserCallback().getQueue())
            .build();
    }
    @Bean
    FanoutExchange rawExchange(RabbitMqProperties properties) {
        return ExchangeBuilder.fanoutExchange(properties.getRawPost().getExchange())
            .durable(true)
            .build();
    }

    @Bean
    Queue rawStorageQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getRawPost().getStorage())
            .build();
    }

    @Bean
    Binding rawStorageBinding(RabbitMqProperties properties) {
        return BindingBuilder
            .bind(rawStorageQueue(properties))
            .to(rawExchange(properties));
    }
}
