package ru.rassafel.foodsharing.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.step.Step;
import ru.rassafel.foodsharing.session.templates.MainTemplates;
import ru.rassafel.foodsharing.session.templates.ProductTemplates;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

import static ru.rassafel.foodsharing.session.util.ProductButtonsUtil.*;

@Component("product-1")
@RequiredArgsConstructor
public class ChooseOperationProductStep implements Step {
    public static final int STEP_INDEX = 1;

    private final UserService userService;
    private final TemplateEngine templateEngine;
    @Value("${bot-config.max-product-count:100}")
    private Integer maxProductCount;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        Long userId = sessionRequest.getFrom().getId();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        user = userService.getUserWithProducts(userId).orElseThrow(() ->
            new NoSuchElementException("Повторный запрос пользователя с продуктами не дал результата"));

        EmbeddedUserSession userSession = user.getUserSession();
        Collection<Product> products = user.getProducts();

        if (message.equalsIgnoreCase(ADD_PRODUCT)) {
            int productCount = products.size();
            if (productCount >= maxProductCount) {
                throw new BotException(userId, templateEngine.compileTemplate(ProductTemplates.TOO_MANY_PRODUCTS,
                    Map.of("count", maxProductCount)));
            }
            responseMessage = templateEngine.compileTemplate(ProductTemplates.PRODUCT_NAME_EXPECTATION);

            userSession.setSessionStep(ChooseNewProductStep.STEP_INDEX);
            responseButtons.addButton(new BotButtons.BotButton(BACK_TO_PRODUCTS));
        } else if (message.equalsIgnoreCase(DELETE_PRODUCT)) {
            if (products.isEmpty()) {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.EMPTY_PRODUCTS);
                responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
            } else {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS_TO_DELETE,
                    ProductTemplates.buildMapOfProducts(products));
                responseButtons.addButton(new BotButtons.BotButton(BACK_TO_PRODUCTS));
                responseButtons.addButton(new BotButtons.BotButton(DELETE_ALL));
                userSession.setSessionStep(DeleteProductStep.STEP_INDEX);
            }
        } else if (message.equalsIgnoreCase(MY_PRODUCTS)) {
            if (products.isEmpty()) {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.EMPTY_PRODUCTS);
            } else {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS,
                    ProductTemplates.buildMapOfProducts(products));
            }
            responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
        } else {
            throw new BotException(userId,
                templateEngine.compileTemplate(MainTemplates.INVALID_OPERATION,
                    MainTemplates.buildMapOfOperations(PRODUCT_MAIN_BUTTONS)));
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);

        userService.saveUser(user);
    }
}
