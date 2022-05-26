package ru.rassafel.foodsharing.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.ProductService;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.step.Step;
import ru.rassafel.foodsharing.session.templates.ProductTemplates;
import ru.rassafel.foodsharing.session.util.ProductButtonsUtil;
import ru.rassafel.foodsharing.session.util.SessionUtil;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static ru.rassafel.foodsharing.session.util.ProductButtonsUtil.*;

@Component("product-4")
@RequiredArgsConstructor
public class DeleteProductStep implements Step {
    public static final int STEP_INDEX = 4;

    private final ProductService productService;
    private final UserService userService;
    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        user = userService.getUserWithProducts(sessionRequest.getFrom().getId()).orElseThrow(() ->
            new NoSuchElementException("Повторный запрос пользователя с продуктами не дал результата"));

        EmbeddedUserSession userSession = user.getUserSession();

        if (DELETE_ALL.equalsIgnoreCase(message)) {
            user.getProducts().clear();
            responseMessage = templateEngine.compileTemplate(ProductTemplates.EMPTY_PRODUCTS_AFTER_DELETE);
            userSession.setSessionStep(ChooseOperationProductStep.STEP_INDEX);
            responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
        } else if (BACK_TO_PRODUCTS.equalsIgnoreCase(message)) {
            responseMessage = templateEngine.compileTemplate(ProductTemplates.BACK_TO_PRODUCTS);
            responseButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);
            userSession.setSessionStep(ChooseOperationProductStep.STEP_INDEX);
        }else {
            Map<Integer, String> usersProductNamesMap = productService.getUsersProductNamesMap(user);
            Set<String> productNamesToDelete;
            try {
                productNamesToDelete = SessionUtil.findValuesByMessage(usersProductNamesMap, message);
            } catch (IllegalArgumentException ex) {
                throw new BotException(user.getId(), ex.getMessage());
            }
            Collection<Product> products = user.getProducts();
            products.removeIf(p -> productNamesToDelete.contains(p.getName()));

            if (products.isEmpty()) {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.EMPTY_PRODUCTS_AFTER_DELETE);
                userSession.setSessionStep(ChooseOperationProductStep.STEP_INDEX);
                responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
            } else {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS_AFTER_DELETE,
                    ProductTemplates.buildMapOfProducts(products));
                responseButtons.addButton(new BotButtons.BotButton(BACK_TO_PRODUCTS));
            }
        }

        userService.saveUser(user);

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);
    }
}
