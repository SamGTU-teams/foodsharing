package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.templates.MainTemplates;
import ru.rassafel.bot.session.templates.ProductTemplates;

import java.util.Map;
import java.util.NoSuchElementException;

import static ru.rassafel.bot.session.util.ProductButtonsUtil.PRODUCT_MAIN_BUTTONS;

@Component("product-1")
@RequiredArgsConstructor
public class ChooseOperationProductStep implements Step {

    public static final int STEP_INDEX = 1;

    private final ProductService productService;
    private final UserService userService;
    private final TemplateEngine templateEngine;
    @Value("${bot-config.max-product-count:100}")
    private Integer maxProductCount;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {

        String message = sessionRequest.getMessage();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        user = userService.getUserWithProducts(sessionRequest.getFrom().getId()).orElseThrow(() ->
            new NoSuchElementException("Повторный запрос пользователя с продуктами не дал результата"));

        EmbeddedUserSession userSession = user.getUserSession();

        if (message.equals("добавить продукт")) {
            int productCount = user.getProducts().size();
            if (productCount >= maxProductCount) {
                throw new BotException(user.getId(), templateEngine.compileTemplate(ProductTemplates.TOO_MANY_PRODUCTS,
                    Map.of("count", maxProductCount)));
            }
            responseMessage = templateEngine.compileTemplate(ProductTemplates.PRODUCT_NAME_EXPECTATION);

            userSession.setSessionStep(ChooseNewProductStep.STEP_INDEX);
        } else if (message.equals("удалить продукт")) {

            if (user.getProducts().isEmpty()) {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.EMPTY_PRODUCTS);
                responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
            } else {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS_TO_DELETE,
                    ProductTemplates.buildMapOfProducts(user.getProducts()));
                responseButtons.addButton(new BotButtons.BotButton("Удалить все"));
                userSession.setSessionStep(DeleteProductStep.STEP_INDEX);
            }
        } else if (message.equals("мои продукты")) {
            if (user.getProducts().isEmpty()) {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.EMPTY_PRODUCTS);
            } else {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS,
                    ProductTemplates.buildMapOfProducts(user.getProducts()));
            }
            responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
        } else {
            throw new BotException(user.getId(),
                templateEngine.compileTemplate(MainTemplates.INVALID_OPERATION,
                    MainTemplates.buildMapOfOperations(PRODUCT_MAIN_BUTTONS)));
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);

        userService.saveUser(user);
    }
}
