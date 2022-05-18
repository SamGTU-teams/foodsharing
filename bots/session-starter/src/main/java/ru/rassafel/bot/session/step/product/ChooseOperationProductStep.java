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
import ru.rassafel.bot.session.templates.ProductTemplates;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.String.format;
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
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        user = userService.getUserWithProducts(sessionRequest.getFrom().getId()).orElseThrow(() ->
            new NoSuchElementException("Повторный запрос пользователя с продуктами не дал результата"));

        if (message.equals("добавить продукт")) {
            int productCount = user.getProducts().size();
            if (productCount >= maxProductCount) {
                throw new BotException(user.getId(), format("Вы не можете добавить больше %d продуктов, сначала удалите несколько", maxProductCount));
            }
            responseMessage = "Введите название продукта, который хотите добавить";

            userSession.setSessionStep(ChooseNewProductStep.STEP_INDEX);
        } else if (message.equals("удалить продукт")) {

            if (user.getProducts().isEmpty()) {
                responseMessage = "У вас нет добавленных продуктов";
                responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
            } else {
                AtomicInteger indexer = new AtomicInteger(1);
                responseMessage = templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS,
                    Map.of("products",
                        user.getProducts().stream().map(product ->
                            Map.of(
                                "index", indexer.getAndIncrement(),
                                "name", product.getName())).collect(Collectors.toList())));
                responseButtons.addButton(new BotButtons.BotButton("Удалить все"));
                userSession.setSessionStep(DeleteProductStep.STEP_INDEX);
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
