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

@Component("product-3")
@RequiredArgsConstructor
public class AddNewProductStep implements Step {
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

        int resultSessionStep = 2;
        if (message.equals("попробовать еще")) {
            responseMessage = "Введите продукт еще раз";
        } else {
            Product productByName = productRepository.findByNameEqualsIgnoreCase(message)
                .orElseThrow(() -> new BotException(user.getId(),
                    "Неверное имя продукта, воспользуйтесь кнопками или попробуйте еще"));
            boolean contains = productService.getUsersProductNames(user).contains(productByName.getName());
            if (contains) {
                responseMessage = "У вас уже есть такой продукт, введите еще";
            } else {
                user.addProduct(productByName);
                responseMessage = "Вы успешно добавили продукт!\n";

                int productCount = user.getProducts().size();
                if (productCount >= maxProductCount) {
                    resultSessionStep = 1;
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
