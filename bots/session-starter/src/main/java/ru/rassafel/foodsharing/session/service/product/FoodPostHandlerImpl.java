package ru.rassafel.foodsharing.session.service.product;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.service.FoodPostHandler;
import ru.rassafel.foodsharing.session.service.Messenger;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.message.templates.MainTemplates;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FoodPostHandlerImpl implements FoodPostHandler {
    private final UserService userService;
    private final TemplateEngine templateEngine;
    private final Messenger messenger;

    @Override
    public void handle(FoodPostDto foodPostDto) {
        userService.findByFoodPost(foodPostDto)
            .stream()
            .map(result -> {
                String message = templateEngine.compileTemplate(MainTemplates.POST_INFO,
                    Map.of(
                        "places", result.getPlaceNames(),
                        "products", result.getProductNames().stream().map(String::toLowerCase).collect(Collectors.toList()),
                        "url", foodPostDto.getUrl(),
                        "text", foodPostDto.getText()
                    ));
                return SessionResponse.builder()
                    .message(message)
                    .sendTo(new To(result.getUserId()))
                    .build();
            })
            .forEach(messenger::send);
    }
}
