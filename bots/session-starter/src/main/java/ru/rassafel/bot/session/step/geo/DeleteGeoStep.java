package ru.rassafel.bot.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.SessionUtil;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.service.PlaceService;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("geo-5")
@RequiredArgsConstructor
public class DeleteGeoStep implements Step {

    private final PlaceService placeService;

    @Override
    public Integer getStepNumber() {
        return 5;
    }

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        Map<Integer, String> usersPlacesNamesMap = placeService.getUsersPlacesNamesMap(user, sessionRequest.getType());
        Set<String> productNamesToDelete = SessionUtil.getAllNames(usersPlacesNamesMap, message);
        Collection<Place> usersPlaces = placeService.findByUserId(user.getId(), sessionRequest.getType());
        for (String placeName : productNamesToDelete) {
            Place place = usersPlaces.stream().filter(p -> p.getName().equals(placeName)).findFirst().orElseThrow(RuntimeException::new);
            placeService.deletePlace(place, sessionRequest.getType());
        }
        sessionResponse.setMessage("Место удалено");

        sessionResponse.setButtons(ButtonsUtil.withBackToMain(GeoButtonsUtil.GEO_MAIN_BUTTONS));
    }


}
