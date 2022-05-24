package ru.rassafel.foodsharing.vkbot.scenarios.place;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rassafel.foodsharing.session.model.dto.From;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.service.PlaceService;
import ru.rassafel.foodsharing.session.service.session.SessionEnum;
import ru.rassafel.foodsharing.session.step.geo.ChooseOperationGeoStep;
import ru.rassafel.foodsharing.session.templates.PlaceTemplates;
import ru.rassafel.foodsharing.session.util.ButtonsUtil;
import ru.rassafel.foodsharing.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.vkbot.scenarios.SpringCucumberSuperTest;

import java.util.ArrayList;
import java.util.List;

import static ru.rassafel.foodsharing.vkbot.Util.*;

public class GetPlaceScenarioTest extends SpringCucumberSuperTest {
    @Autowired
    PlaceService placeService;

    @Then("user with id {long} wants to get his places and types {string} and expect {int} places")
    public void user_with_id_wants_to_get_his_places(Long userId, String message, int expected) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), ChooseOperationGeoStep.STEP_INDEX, true);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST,
            PlaceTemplates.buildMapOfPlaces(placeService.findByUserId(userId))));

        List<String> geoMainButtons = new ArrayList<>(GeoButtonsUtil.GEO_MAIN_BUTTONS);
        geoMainButtons.add(0, ButtonsUtil.BACK_TO_MAIN_MENU);
        assertButtons(response, geoMainButtons);
    }
}
