package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
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
import ru.rassafel.bot.session.templates.ProductTemplates;
import ru.rassafel.bot.session.util.SessionUtil;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static ru.rassafel.bot.session.util.ProductButtonsUtil.PRODUCT_MAIN_BUTTONS;

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

        if ("удалить все".equals(message)) {
            user.getProducts().clear();
            responseMessage = templateEngine.compileTemplate(ProductTemplates.EMPTY_PRODUCTS_AFTER_DELETE);
            userSession.setSessionStep(ChooseOperationProductStep.STEP_INDEX);
            responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
        } else {
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
            }
        }

        userService.saveUser(user);

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);
    }
}
