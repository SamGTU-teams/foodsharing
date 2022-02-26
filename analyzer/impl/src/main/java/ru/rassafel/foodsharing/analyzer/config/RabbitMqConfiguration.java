package ru.rassafel.foodsharing.analyzer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfiguration {
    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    FanoutExchange consumerExchange(RabbitMqProperties properties) {
        return new FanoutExchange(properties.getConsumer().getExchange());
    }

    @Bean
    FanoutExchange producerExchange(RabbitMqProperties properties) {
        return new FanoutExchange(properties.getProducer().getExchange());
    }

    @Bean
    Queue consumerQueue(RabbitMqProperties properties) {
        return new Queue(properties.getConsumer().getQueue());
    }

    @Bean
    Binding consumerBinding(Queue consumerQueue, FanoutExchange consumerExchange) {
        return BindingBuilder.bind(consumerQueue).to(consumerExchange);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter jsonMessageConverter,
                                  RabbitMqProperties properties) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(properties.getProducer().getExchange());
        return rabbitTemplate;
    }
}
