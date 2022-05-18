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
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.ProductButtonsUtil;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component("product-3")
@RequiredArgsConstructor
public class AddNewProductStep implements Step {

    public static final int STEP_INDEX = 3;

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserService userService;
    @Value("${bot-config.max-product-count:100}")
    private Integer maxProductCount;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        int resultSessionStep = ChooseNewProductStep.STEP_INDEX;
        if (message.equals("попробовать еще")) {
            responseMessage = "Введите продукт еще раз";
        } else {
            Optional<Product> byName = productRepository.findByNameEqualsIgnoreCase(message);
            if(byName.isEmpty()){
                throw new BotException(user.getId(),
                    "Неверное имя продукта, воспользуйтесь кнопками или попробуйте еще");
            }
            Product productByName = byName.get();

            user = userService.getUserWithProducts(sessionRequest.getFrom().getId()).orElseThrow(() ->
                new NoSuchElementException("Повторный запрос пользователя с продуктами не дал результата"));

            boolean contains = productService.getUsersProductNames(user).contains(productByName.getName());
            if (contains) {
                responseMessage = "У вас уже есть такой продукт, введите еще";
            } else {
                user.addProduct(productByName);
                responseMessage = "Вы успешно добавили продукт!\n";

                int productCount = user.getProducts().size();
                if (productCount >= maxProductCount) {
                    resultSessionStep = ChooseOperationProductStep.STEP_INDEX;
                    responseButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);
                    responseMessage += String.format("Вы добавили максимальное количество продуктов, больше %d добавить нельзя", maxProductCount);
                } else {
                    responseMessage += "Введите еще";
                }
            }
        }
        userSession.setSessionStep(resultSessionStep);

        sessionResponse.setMessage(responseMessage);
        sessionResponse.setButtons(responseButtons);

        userService.saveUser(user);
    }
}
