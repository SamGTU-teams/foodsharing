package ru.rassafel.bot.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.rassafel.bot.session.util.GeoButtonsUtil.GEO_MAIN_BUTTONS;

@Component("geo-1")
@RequiredArgsConstructor
public class ChooseOperationGeoStep implements Step {

    public static final int STEP_INDEX = 1;

    private final PlaceService placeService;
    private final UserService userService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        if (message.equals("мои места")) {
            Collection<Place> points = placeService.findByUserId(user.getId());
            if (points.isEmpty()) {

                responseMessage = "У вас нет добавленных мест";

            } else {
                AtomicInteger counter = new AtomicInteger(1);
                String pointText = points.stream().map(p -> String.format(
                        "%d. Название места : %s\n" +
                            "Полный адрес места : %s\n" +
                            "Радиус поиска вокруг этого места : %d\n",
                        counter.getAndIncrement(), p.getName(), p.getAddress(), p.getRadius()))
                    .collect(Collectors.joining("\n"));

                responseMessage = "Ваши места : \n" + pointText;
            }

            responseButtons.addAll(GEO_MAIN_BUTTONS);

        } else if (message.equals("добавить место")) {

            responseMessage = "Отправьте мне точку на карте";
            responseButtons.addButton(BotButtons.BotButton.GEO_BUTTON);

            userSession.setSessionStep(AddNewPlaceGeoStep.STEP_INDEX);
        } else if (message.equals("удалить место")) {
            Collection<Place> points = placeService.findByUserId(user.getId());
            if (points.isEmpty()) {

                responseMessage = "У вас пока нет добавленных мест";
                responseButtons.addAll(GEO_MAIN_BUTTONS);

            } else {

                responseMessage = placeService.getUsersPlaceMapMessage(user,
                    "Вот список ваших мест, напишите название или номер(а) того которого хотите удалить, пример: 1,2,3\n" +
                        "Или вы можете удалить все ваши места");

                responseButtons.addButton(new BotButtons.BotButton("Удалить все"));
                userSession.setSessionStep(5);
            }
        } else if (message.equals("редактирование места")) {
            Collection<Place> points = placeService.findByUserId(user.getId());

            if (points.isEmpty()) {
                responseMessage = "У вас пока нет добавленных мест";
                responseButtons.addAll(GEO_MAIN_BUTTONS);
            } else {
                responseMessage = placeService.getUsersPlaceMapMessage(user,
                    "\n\nВот список ваших мест, напишите название или номер, того которого хотите отредактировать, пример: 1 или Дом");

                userSession.setSessionStep(EditGeoStep.STEP_INDEX);
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
