package ru.rassafel.bot.session.util;

import java.util.List;

public interface ButtonsUtil {

    String BACK_TO_MAIN_MENU = "На главную",
        PRODUCTS = "Продукты",
        PLACES = "Места";
    List<String> DEFAULT_BUTTONS = List.of(PRODUCTS, PLACES);
}
