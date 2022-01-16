package ru.rassafel.foodsharing.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.bot.message.listener.MessageListener;

import java.util.List;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class BotRabbitMqAutoConfiguration {
    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    FanoutExchange fanoutExchange(RabbitMqProperties properties) {
        return new FanoutExchange(properties.getExchangeName());
    }

    @Bean
    Queue queue(RabbitMqProperties properties) {
        return new Queue(properties.getQueueName());
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageListener<?> listener,
                                           MessageConverter messageConverter) {
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter();
        listenerAdapter.setDelegate(listener);
        listenerAdapter.setDefaultListenerMethod("receiveMessage");
        listenerAdapter.setMessageConverter(messageConverter);
        return listenerAdapter;
    }

    @Bean
    SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                            MessageListenerAdapter listenerAdapter,
                                                            List<Queue> queues) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queues.toArray(new Queue[0]));
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    @ConditionalOnMissingBean(MessageListener.class)
    MessageListener<String> defaultMessageListener() {
        return new DefaultListener();
    }

    @Slf4j
    static class DefaultListener implements MessageListener<String> {
        @Override
        public void receiveMessage(String message) {
            log.info("Incoming message: {}", message);
        }
    }
}
