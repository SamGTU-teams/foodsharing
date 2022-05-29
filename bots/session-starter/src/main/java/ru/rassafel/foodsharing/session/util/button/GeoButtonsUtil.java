package ru.rassafel.foodsharing.session.util.button;

import java.util.List;

public interface GeoButtonsUtil {
    String
        MY_PLACES = "Мои места",
        ADD_PLACE = "Добавить место",
        DELETE_PLACE = "Удалить место",
        EDIT_PLACE = "Редактировать место",
        DELETE_ALL = "Удалить все",
        LEAVE_RADIUS_AS_IS = "Оставить как есть",
        BACK_TO_PLACES = "Назад к местам";

    List<String> GEO_MAIN_BUTTONS = List.of(
        MY_PLACES,
        ADD_PLACE,
        EDIT_PLACE,
        DELETE_PLACE
    );
}
