package ru.rassafel.bot.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.model.entity.place.Place;
import ru.rassafel.bot.session.model.entity.user.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.rassafel.bot.session.util.GeoButtonsUtil.GEO_MAIN_BUTTONS;

@Component("geo-1")
@RequiredArgsConstructor
public class ChooseOperationGeoStep implements Step {
    private final PlaceService placeService;
    private final UserService userService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        if (message.equals("мои места")) {
            Collection<Place> points = placeService.findByUserId(user.getId(), sessionRequest.getType());
            if (points.isEmpty()) {

                responseMessage = "У вас нет добавленных мест";

            } else {
                int[] i = {1};
                String pointText = points.stream().map(p -> String.format(
                        "%d. Название места : %s\n" +
                            "Координаты места : %f - широта, %f - долгота\n" +
                            "Радиус поиска вокруг этого места : %d\n",
                        i[0]++, p.getName(), p.getGeo().getLat(), p.getGeo().getLon(), p.getRadius()))
                    .collect(Collectors.joining("\n"));

                responseMessage = "Ваши места : \n" + pointText;
            }

            responseButtons.addAll(GEO_MAIN_BUTTONS);

        } else if (message.equals("добавить место")) {

            responseMessage = "Отправьте мне точку на карте";
            responseButtons.addButton(BotButtons.BotButton.GEO_BUTTON);

            userSession.setSessionStep(2);
        } else if (message.equals("удалить место")) {
            Collection<Place> points = placeService.findByUserId(user.getId(), sessionRequest.getType());
            if (points.isEmpty()) {

                responseMessage = "У вас пока нет добавленных мест";
                responseButtons.addAll(GEO_MAIN_BUTTONS);

            } else {

                responseMessage = placeService.getUsersPlaceMapMessage(user, sessionRequest.getType(),
                    "Вот список ваших мест, напишите название или номер(а) того которого хотите удалить, пример: 1,2,3");

                userSession.setSessionStep(5);
            }
        } else if (message.equals("редактирование места")) {
            Collection<Place> points = placeService.findByUserId(user.getId(), sessionRequest.getType());

            if (points.isEmpty()) {
                responseMessage = "У вас пока нет добавленных мест";
                responseButtons.addAll(GEO_MAIN_BUTTONS);
            } else {
                responseMessage = placeService.getUsersPlaceMapMessage(user, sessionRequest.getType(),
                    "\n\nВот список ваших мест, напишите название или номер, того которого хотите отредактировать, пример: 1 или Дом");

                userSession.setSessionStep(6);
            }
        } else {
            throw new BotException(user.getId(), "На этом этапе доступны только следующие команды " +
                String.join("\n", GEO_MAIN_BUTTONS));
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);

        userService.saveUser(user);
    }
}
