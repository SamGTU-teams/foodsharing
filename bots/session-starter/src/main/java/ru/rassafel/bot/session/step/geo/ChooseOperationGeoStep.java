package ru.rassafel.bot.session.step.geo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.foodsharing.common.service.PlaceService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("geo-1")
@RequiredArgsConstructor
public class ChooseOperationGeoStep implements Step {

    private final PlaceService placeService;

    @Override
    public Integer getStepNumber() {
        return 1;
    }

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        List<String> responseButtons;

        if (message.equals("мои места")) {
            Collection<Place> points = placeService.findByUserId(user.getId(), sessionRequest.getType());
            if (points.isEmpty()) {

                responseMessage = "У вас нет добавленных мест";

            } else {
                int[] i = {1};
                String pointText = points.stream().map(p -> String.format("""
                        %d. Название места : %s
                        Координаты места : %f - широта, %f - долгота
                        Радиус поиска вокруг этого места : %d
                        """, i[0]++, p.getName(), p.getGeo().getLat(), p.getGeo().getLon(), p.getRadius()))
                        .collect(Collectors.joining("\n"));

                responseMessage = "Ваши места : \n" + pointText;
            }

            responseButtons = ButtonsUtil.withBackToMain(GeoButtonsUtil.GEO_MAIN_BUTTONS);

        } else if (message.equals("добавить место")) {

            responseMessage = "Отправьте мне точку на карте";
            responseButtons = ButtonsUtil.BACK_TO_MAIN;

            userSession.setSessionStep(2);
        } else if (message.equals("удалить место")) {
            Collection<Place> points = placeService.findByUserId(user.getId(), sessionRequest.getType());
            if (points.isEmpty()) {

                responseMessage = "У вас пока нет добавленных мест";
                responseButtons = ButtonsUtil.withBackToMain(GeoButtonsUtil.GEO_MAIN_BUTTONS);

            } else {

                responseMessage = placeService.getUsersPlacesNamesMap(user, sessionRequest.getType()).entrySet().stream().map(entry -> entry.getKey() + "." + entry.getValue())
                    .collect(Collectors.joining("\n"))
                    + "\n\nВот список ваших мест, напишите название или номер(а) того которого хотите удалить, пример: 1,2,3";

                responseButtons = null;
                userSession.setSessionStep(5);
            }
        } else if (message.equals("редактирование места")) {
            Collection<Place> points = placeService.findByUserId(user.getId(), sessionRequest.getType());

            if (points.isEmpty()) {
                responseMessage = "У вас пока нет добавленных мест";
                responseButtons = ButtonsUtil.withBackToMain(GeoButtonsUtil.GEO_MAIN_BUTTONS);
            } else {
                responseMessage = "Выберите место у которого хотите отредактировать радиус поиска";
                responseButtons = points.stream().map(Place::getName).collect(Collectors.toList());

                userSession.setSessionStep(6);
            }
        }else {
//            responseMessage = "На этом этапе доступны только следующие команды";
//            responseButtons = ButtonsUtil.withBackToMain(GeoButtonsUtil.GEO_MAIN_BUTTONS);
            throw new BotException(user.getId(), "На этом этапе доступны только следующие команды");
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);
    }
}
