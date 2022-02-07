package ru.rassafel.foodsharing.ibot.message.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.bot.message.listener.MessageListener;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class FoodPostListener implements MessageListener<FoodPostDto> {
    @Override
    public void receiveMessage(FoodPostDto message) {
        log.info("Accepted message: {}", message);
    }
}
