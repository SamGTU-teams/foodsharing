package ru.rassafel.foodsharing.session.util;

import java.util.List;

public interface ProductButtonsUtil {

    String ADD_PRODUCT = "Добавить продукт",
        DELETE_PRODUCT = "Удалить продукт",
        MY_PRODUCTS = "Мои продукты",
        DELETE_ALL = "Удалить все",
        TRY_MORE = "Попробовать еще";

    List<String> PRODUCT_MAIN_BUTTONS = List.of(
        ADD_PRODUCT,
        DELETE_PRODUCT,
        MY_PRODUCTS);
}
