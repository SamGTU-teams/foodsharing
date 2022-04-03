package ru.rassafel.bot.session.type.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.dto.To;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.bot.session.util.SessionUtil;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

import static ru.rassafel.bot.session.util.ButtonsUtil.addBackToMainButton;
import static ru.rassafel.bot.session.util.ProductButtonsUtil.PRODUCT_MAIN_BUTTONS;

@Component
@RequiredArgsConstructor
public class ProductSession implements BotSession {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public SessionResponse execute(SessionRequest request, User user) {
        final String message = request.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();
        int step = userSession.getSessionStep();

        BotButtons responseButtons = new BotButtons();
        String responseMessage;

        if (step == 0) {

            responseMessage = "Выберите:";
            responseButtons.addAll(PRODUCT_MAIN_BUTTONS);

            userSession.setSessionStep(1);
        } else if (step == 1) {

            if (message.equals("добавить продукт")) {

                responseMessage = "Введите название продукта, который хотите добавить";

                userSession.setSessionStep(2);
            } else if (message.equals("удалить продукт")) {

                if (user.getProducts().isEmpty()) {
                    responseMessage = "У вас нет добавленных продуктов";
                    responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
                } else {

                    responseMessage = getUsersProductNamesMap(user).entrySet().stream().map(entry -> entry.getKey() + "." + entry.getValue())
                        .collect(Collectors.joining("\n"))
                        + "\n\nВот список ваших продуктов, напишите название или номер(а) того которого хотите удалить, пример: 1,2,3";

                    userSession.setSessionStep(4);
                }
            } else if (message.equals("мои продукты")) {
                if (user.getProducts().isEmpty()) {
                    responseMessage = "У вас еще нет добавленных продуктов";
                } else {
                    responseMessage = "Ваши продукты: \n" + String.join("\n", getUsersProductNames(user));
                }
                responseButtons.addAll(PRODUCT_MAIN_BUTTONS);
            } else {
                throw new BotException(user.getId(), """
                    На этом этапе доступны только следующие команды :
                    Добавить продукт
                    Удалить продукт
                    Мои продукты
                    На главную""");
            }
            //Список предлагаемых продуктов
        } else if (step == 2) {

            //Поиск подходящих продуктов
            List<String> similarProducts = productService.getSimilarProducts(message);

            if (similarProducts.isEmpty()) {
                responseMessage = "Такого продукта не нашлось";

                userSession.setSessionStep(1);

            } else {
                responseMessage = "Возможно вы имели ввиду следующие продукты, выберите какой из них вы хотите добавить или попробуйте ввести еще";
                responseButtons.addButton(new BotButtons.BotButton("Попробовать еще")).addAll(similarProducts);

                userSession.setSessionStep(3);
            }
            //Добавление продуктов
        } else if (step == 3) {
            Product productByName = productRepository.findByName(message).orElseThrow(() ->
                new BotException(user.getId(), "Вы ввели неправильное имя продукта, попробуйте еще"));
            boolean contains = getUsersProductNames(user).contains(productByName.getName());
            if (contains) {
                responseMessage = "У вас уже есть такой продукт, введите еще";
            } else {
                user.addProduct(productByName);
                responseMessage = "Вы успешно добавили продукт! Введите еще";
            }
            userSession.setSessionStep(2);
            //Удаление продуктов
        } else if (step == 4) {
            Map<Integer, String> usersProductNamesMap = getUsersProductNamesMap(user);
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
                getUsersProductNamesMap(user).entrySet().stream().map(entry -> entry.getKey() + "." + entry.getValue())
                    .collect(Collectors.joining("\n"));
            }
        } else {
            throw new IllegalArgumentException("Step not found!");
        }


        return SessionResponse.builder()
            .sendTo(To.builder()
                .id(user.getId())
                .build())
            .buttons(responseButtons)
            .message(responseMessage)
            .build();
    }

    @Override
    public String getName() {
        return "productSession";
    }

    private List<String> getUsersProductNames(User user) {
        return user.getProducts().stream().map(Product::getName).collect(Collectors.toList());
    }

    private Map<Integer, String> getUsersProductNamesMap(User user) {
        int[] productCounter = {1};
        return user.getProducts().stream().map(Product::getName).collect(Collectors.toMap(
            o -> productCounter[0]++,
            o -> o
        ));
    }
}
