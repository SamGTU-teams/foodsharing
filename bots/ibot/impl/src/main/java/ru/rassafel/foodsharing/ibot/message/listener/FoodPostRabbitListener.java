package ru.rassafel.foodsharing.ibot.message.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;
import ru.rassafel.foodsharing.ibot.model.mapper.FoodPostMapper;
import ru.rassafel.foodsharing.ibot.repository.FoodPostRepository;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.ready-post.queue}"})
public class FoodPostRabbitListener {
    private final FoodPostRepository repository;

    private final FoodPostMapper mapper;

    @RabbitHandler
    public void receiveMessage(FoodPostDto foodPostDto) {
        log.debug("Accepted message: {}", foodPostDto);

        FoodPost foodPost = mapper.dtoToEntity(foodPostDto);
        repository.save(foodPost);
    }
}
