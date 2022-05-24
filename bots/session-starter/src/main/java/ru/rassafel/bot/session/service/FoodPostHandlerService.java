package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.templates.MainTemplates;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FoodPostHandlerService {
    private final UserService userService;
    private final Messenger messenger;
    private final TemplateEngine templateEngine;

    public void handleFoodPostReceived(FoodPostDto foodPostDto) {
        userService.findByFoodPost(foodPostDto).forEach(result -> {
            String resultMessage = templateEngine.compileTemplate(MainTemplates.POST_INFO,
                Map.of(
                    "places", result.getPlaceNames(),
                    "products", result.getProductNames(),
                    "url", foodPostDto.getUrl(),
                    "text", foodPostDto.getText()
                ));
            messenger.send(resultMessage, result.getUserId().intValue());
        });
    }
}
