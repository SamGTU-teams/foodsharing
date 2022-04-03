package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.model.entity.user.User;

import java.util.List;

@Component("product-2")
@RequiredArgsConstructor
public class ChooseNewProductStep implements Step {

    private final ProductService productService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        //Поиск подходящих продуктов
        List<String> similarProducts = productService.getSimilarProducts(message);

        if (similarProducts.isEmpty()) {
            responseMessage = "Такого продукта не нашлось";

            userSession.setSessionStep(1);

        } else {
            responseMessage = "Возможно вы имели ввиду следующие продукты, выберите какой из них вы хотите добавить или попробуйте ввести еще";
            responseButtons.addButton(new BotButtons.BotButton("Попробовать еще")).addAll(similarProducts);

            userSession.setSessionStep(3);
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);
    }
}
