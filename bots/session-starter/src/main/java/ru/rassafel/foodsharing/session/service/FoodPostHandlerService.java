package ru.rassafel.foodsharing.session.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.templates.MainTemplates;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodPostHandlerService {
    private final UserService userService;
    private final TemplateEngine templateEngine;
    private BlockingQueue<SessionResponse> queue;

    public void handleFoodPostReceived(FoodPostDto foodPostDto) {
        userService.findByFoodPost(foodPostDto).forEach(result -> {
            String resultMessage = templateEngine.compileTemplate(MainTemplates.POST_INFO,
                Map.of(
                    "places", result.getPlaceNames(),
                    "products", result.getProductNames().stream().map(String::toLowerCase).collect(Collectors.toList()),
                    "url", foodPostDto.getUrl(),
                    "text", foodPostDto.getText()
                ));
            try {
                queue.put(SessionResponse.builder()
                        .message(resultMessage)
                        .sendTo(new To(result.getUserId()))
                    .build());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
