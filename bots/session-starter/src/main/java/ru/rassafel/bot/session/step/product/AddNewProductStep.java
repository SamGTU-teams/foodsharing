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
import ru.rassafel.bot.session.repository.ProductRepository;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.templates.ProductTemplates;
import ru.rassafel.bot.session.util.ProductButtonsUtil;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ru.rassafel.bot.session.util.ProductButtonsUtil.TRY_MORE;

@Component("product-3")
@RequiredArgsConstructor
public class AddNewProductStep implements Step {

    public static final int STEP_INDEX = 3;

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserService userService;
    @Value("${bot-config.max-product-count:100}")
    private Integer maxProductCount;

    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        int resultSessionStep = ChooseNewProductStep.STEP_INDEX;
        if (message.equalsIgnoreCase(TRY_MORE)) {
            responseMessage = "Введите продукт еще раз";
        } else {
            Optional<Product> byName = productRepository.findByNameEqualsIgnoreCase(message);
            if(byName.isEmpty()){
                throw new BotException(user.getId(),
                    templateEngine.compileTemplate(ProductTemplates.INVALID_PRODUCT_NAME));
            }
            Product productByName = byName.get();

            user = userService.getUserWithProducts(sessionRequest.getFrom().getId()).orElseThrow(() ->
                new NoSuchElementException("Повторный запрос пользователя с продуктами не дал результата"));

            userSession = user.getUserSession();

            boolean contains = productService.getUsersProductNames(user).contains(productByName.getName());
            if (contains) {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.PRODUCT_ALREADY_EXISTS);
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
                }
            }
        }
        userSession.setSessionStep(resultSessionStep);

        sessionResponse.setMessage(responseMessage);
        sessionResponse.setButtons(responseButtons);

        userService.saveUser(user);
    }
}
