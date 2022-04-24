package ru.rassafel.bot.session.util;

import java.util.ArrayList;
import java.util.List;

public class ButtonsUtil {

    public static List<String> DEFAULT_BUTTONS = List.of("Продукты", "Места");

    public static List<String> BACK_TO_MAIN = List.of("На главную");

    public static List<String> addBackToMainButton(List<String> buttons) {
        buttons.add(0, "На главную");
        return buttons;
    }

    public static List<String> getModifiableList(List<String> buttons) {
        return new ArrayList<>(buttons);
    }

    public static List<String> withBackToMain(List<String> buttons) {
        List<String> modifiableList = getModifiableList(buttons);
        modifiableList.add(0, "На главную");
        return modifiableList;
    }
}
