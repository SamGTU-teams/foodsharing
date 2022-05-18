package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.repository.ProductRepository;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.templates.ProductTemplates;

import java.util.List;

@Component("product-2")
@RequiredArgsConstructor
public class ChooseNewProductStep implements Step {

    public static final int STEP_INDEX = 2;

    private final ProductService productService;
    private final UserService userService;
    private final ProductRepository productRepository;

    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        //Поиск подходящих продуктов
        List<String> similarProducts = productService.getSimilarToTextProducts(message);

        if (similarProducts.isEmpty()) {
            responseMessage = "Такого продукта не нашлось, попробуйте снова";
        } else {
            responseMessage = templateEngine.compileTemplate(ProductTemplates.POSSIBLE_PRODUCT_NAMES);
            responseButtons.addButton(new BotButtons.BotButton("Попробовать еще")).addAll(similarProducts);

            userSession.setSessionStep(AddNewProductStep.STEP_INDEX);
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);

        userService.saveUser(user);
    }
}
