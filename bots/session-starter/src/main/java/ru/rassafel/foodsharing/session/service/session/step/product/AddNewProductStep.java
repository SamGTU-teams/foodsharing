package ru.rassafel.foodsharing.session.service.session.step.product;

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
import ru.rassafel.foodsharing.session.repository.ProductRepository;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.product.ProductServiceImpl;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.session.step.Step;
import ru.rassafel.foodsharing.session.service.message.templates.ProductTemplates;
import ru.rassafel.foodsharing.session.util.button.ProductButtonsUtil;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ru.rassafel.foodsharing.session.util.button.ProductButtonsUtil.BACK_TO_PRODUCTS;
import static ru.rassafel.foodsharing.session.util.button.ProductButtonsUtil.TRY_MORE;

@Component("product-3")
@RequiredArgsConstructor
public class AddNewProductStep implements Step {
    public static final int STEP_INDEX = 3;

    private final ProductServiceImpl productService;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final TemplateEngine templateEngine;
    @Value("${bot-config.max-product-count:100}")
    private Integer maxProductCount;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        int resultSessionStep = ChooseNewProductStep.STEP_INDEX;
        if (message.equalsIgnoreCase(TRY_MORE)) {
            responseMessage = templateEngine.compileTemplate(ProductTemplates.TRY_CHOOSE_PRODUCT_MORE);
            responseButtons.addButton(new BotButtons.BotButton(BACK_TO_PRODUCTS));
        } else if (BACK_TO_PRODUCTS.equalsIgnoreCase(message)) {
            resultSessionStep = ChooseOperationProductStep.STEP_INDEX;
            responseMessage = templateEngine.compileTemplate(ProductTemplates.BACK_TO_PRODUCTS);
            responseButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);
        } else {
            Optional<Product> byName = productRepository.findByNameEqualsIgnoreCase(message);
            if (byName.isEmpty()) {
                throw new BotException(user.getId(),
                    templateEngine.compileTemplate(ProductTemplates.INVALID_PRODUCT_NAME));
            }
            Product productByName = byName.get();

            user = userService.getUserWithProducts(sessionRequest.getFrom().getId()).orElseThrow(() ->
                new NoSuchElementException("?????????????????? ???????????? ???????????????????????? ?? ???????????????????? ???? ?????? ????????????????????"));

            userSession = user.getUserSession();

            boolean contains = productService.getUsersProductNames(user).contains(productByName.getName());
            if (contains) {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.PRODUCT_ALREADY_EXISTS);
                responseButtons.addButton(new BotButtons.BotButton(BACK_TO_PRODUCTS));
            } else {
                user.addProduct(productByName);

                int productCount = user.getProducts().size();
                if (productCount >= maxProductCount) {
                    resultSessionStep = ChooseOperationProductStep.STEP_INDEX;
                    responseButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);
                    responseMessage = templateEngine.compileTemplate(ProductTemplates.SUCCESS_ADD_PRODUCT_AND_QUIT,
                        Map.of("maxProductCount", maxProductCount));
                } else {
                    responseMessage = templateEngine.compileTemplate(ProductTemplates.SUCCESS_ADD_PRODUCT);
                    responseButtons.addButton(new BotButtons.BotButton(BACK_TO_PRODUCTS));
                }
            }

        }
        userSession.setSessionStep(resultSessionStep);

        sessionResponse.setMessage(responseMessage);
        sessionResponse.setButtons(responseButtons);

        userService.saveUser(user);
    }
}
