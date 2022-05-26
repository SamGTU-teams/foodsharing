package ru.rassafel.foodsharing.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.repository.ProductRepository;
import ru.rassafel.foodsharing.session.service.ProductService;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.step.Step;
import ru.rassafel.foodsharing.session.templates.ProductTemplates;
import ru.rassafel.foodsharing.session.util.ProductButtonsUtil;

import java.util.List;

import static ru.rassafel.foodsharing.session.util.ProductButtonsUtil.BACK_TO_PRODUCTS;
import static ru.rassafel.foodsharing.session.util.ProductButtonsUtil.TRY_MORE;

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

        if(BACK_TO_PRODUCTS.equalsIgnoreCase(message)){
            userSession.setSessionStep(ChooseOperationProductStep.STEP_INDEX);
            responseMessage = templateEngine.compileTemplate(ProductTemplates.BACK_TO_PRODUCTS);
            responseButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);
        }else {
            if(message.isEmpty()){
                throw new BotException(user.getId(), templateEngine.compileTemplate(ProductTemplates.PRODUCT_NAME_IS_EMPTY));
            }
            responseButtons.addButton(new BotButtons.BotButton(BACK_TO_PRODUCTS));

            //Поиск подходящих продуктов
            List<String> similarProducts = productService.getSimilarToTextProducts(message);

            if (similarProducts.isEmpty()) {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.PRODUCT_NOT_FOUND);
            } else {
                responseMessage = templateEngine.compileTemplate(ProductTemplates.POSSIBLE_PRODUCT_NAMES);
                responseButtons.addButton(new BotButtons.BotButton(TRY_MORE)).addAll(similarProducts);

                userSession.setSessionStep(AddNewProductStep.STEP_INDEX);
            }
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);

        userService.saveUser(user);
    }
}
