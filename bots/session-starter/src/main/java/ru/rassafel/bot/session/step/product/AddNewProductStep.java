package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.repository.ProductRepository;

@Component("product-3")
@RequiredArgsConstructor
public class AddNewProductStep implements Step {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ChooseNewProductStep chooseNewProductStep;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {

        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

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
                responseMessage = "Вы успешно добавили продукт! Введите еще";
            }
        }
        userSession.setSessionStep(2);

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);
    }
}
