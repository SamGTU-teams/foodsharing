package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.SessionUtil;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.bot.session.model.entity.user.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.service.UserService;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static ru.rassafel.bot.session.util.ProductButtonsUtil.PRODUCT_MAIN_BUTTONS;

@Component("product-4")
@RequiredArgsConstructor
public class DeleteProductStep implements Step {

    private final ProductService productService;
    private final UserService userService;


    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {

        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        Map<Integer, String> usersProductNamesMap = productService.getUsersProductNamesMap(user);
        Set<String> productNamesToDelete;
        try {
            productNamesToDelete = SessionUtil.getAllNames(usersProductNamesMap, message);
        } catch (IllegalArgumentException ex) {
            throw new BotException(user.getId(), ex.getMessage());
        }
        Collection<Product> products = user.getProducts();
        for (String prodName : productNamesToDelete) {
            Product product = products.stream().filter(p -> p.getName().equalsIgnoreCase(prodName)).findFirst().orElseThrow(RuntimeException::new);
            products.remove(product);
        }

        if(products.isEmpty()) {
            responseMessage = "Продукты удалены, у вас больше не осталось продуктов";
            userSession.setSessionStep(1);
            responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
        }else {
            responseMessage = "Продукты удалены, введите еще\n\n" +
                productService.getUsersProductNamesMapMessage(user);
        }

        userService.saveUser(user);

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);
    }
}
