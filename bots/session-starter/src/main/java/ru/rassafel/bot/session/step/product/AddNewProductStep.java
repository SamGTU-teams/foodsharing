package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.ProductButtonsUtil;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.repository.ProductRepository;
import ru.rassafel.foodsharing.common.service.UserService;

@Component("product-3")
@RequiredArgsConstructor
public class AddNewProductStep implements Step {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserService userService;


    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {

        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        int resultSessionStep = 2;
        if(message.equals("попробовать еще")){
            responseMessage = "Введите продукт еще раз";
        }else {

            Product productByName = productRepository.findByName(message).orElseThrow(() ->
                new BotException(user.getId(), "Вы ввели неправильное имя продукта, попробуйте еще"));
            boolean contains = productService.getUsersProductNames(user).contains(productByName.getName());
            if (contains) {
                responseMessage = "У вас уже есть такой продукт, введите еще";
            } else {
                user.addProduct(productByName);
                responseMessage = "Вы успешно добавили продукт!\n";

                int productCount = user.getProducts().size();
                if(productCount > 2){
                    responseMessage += "Вы не можете добавить более 100 продуктов, удалите несколько чтобы добавить еще";
                    resultSessionStep = 1;
                    responseButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);
                }else{
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
