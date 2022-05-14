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
import ru.rassafel.bot.session.step.Step;

import static java.lang.String.format;
import static ru.rassafel.bot.session.util.ProductButtonsUtil.PRODUCT_MAIN_BUTTONS;

@Component("product-1")
@RequiredArgsConstructor
public class ChooseOperationProductStep implements Step {
    private final ProductService productService;
    private final UserService userService;
    @Value("${bot-config.max-product-count:2}")
    private Integer maxProductCount;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {

        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        if (message.equals("добавить продукт")) {
            int productCount = user.getProducts().size();
            if (productCount >= maxProductCount) {
                throw new BotException(user.getId(), format("Вы не можете добавить больше %d продуктов, сначала удалите несколько", maxProductCount));
            }
            responseMessage = "Введите название продукта, который хотите добавить";

            userSession.setSessionStep(2);
        } else if (message.equals("удалить продукт")) {

            if (user.getProducts().isEmpty()) {
                responseMessage = "У вас нет добавленных продуктов";
                responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
            } else {

                responseMessage = productService.getUsersProductNamesMapMessage(user,
                    "Вот список ваших продуктов, напишите название или номер(а) того которого хотите удалить, пример: 1,2,3");

                userSession.setSessionStep(4);
            }
        } else if (message.equals("мои продукты")) {
            if (user.getProducts().isEmpty()) {
                responseMessage = "У вас еще нет добавленных продуктов";
            } else {
                responseMessage = "Ваши продукты: \n" + String.join("\n", productService.getUsersProductNames(user));
            }
            responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
        } else {
            throw new BotException(user.getId(),
                "На этом этапе доступны только следующие команды :\n" +
                    "Добавить продукт\n" +
                    "Удалить продукт\n" +
                    "Мои продукты\n" +
                    "На главную");
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);

        userService.saveUser(user);
    }
}
