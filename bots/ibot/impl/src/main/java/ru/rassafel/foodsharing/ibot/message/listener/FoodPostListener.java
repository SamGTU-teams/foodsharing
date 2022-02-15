package ru.rassafel.foodsharing.ibot.message.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.bot.message.listener.MessageListener;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;
import ru.rassafel.foodsharing.ibot.model.mapper.FoodPostMapper;
import ru.rassafel.foodsharing.ibot.repository.FoodPostRepository;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class FoodPostListener implements MessageListener<FoodPostDto> {
    private final FoodPostRepository repository;

    private final FoodPostMapper mapper;

    @Override
    public void receiveMessage(FoodPostDto foodPostDto) {
        log.debug("Accepted message: {}", foodPostDto);

        FoodPost foodPost = mapper.dtoToEntity(foodPostDto);
        repository.save(foodPost);
    }
}
