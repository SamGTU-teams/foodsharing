package ru.rassafel.foodsharing.vkbot.scenarios.place;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rassafel.foodsharing.session.model.dto.From;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.Place;
import ru.rassafel.foodsharing.session.service.PlaceService;
import ru.rassafel.foodsharing.session.service.session.SessionEnum;
import ru.rassafel.foodsharing.session.step.geo.ChooseOperationGeoStep;
import ru.rassafel.foodsharing.session.step.geo.DeleteGeoStep;
import ru.rassafel.foodsharing.session.templates.PlaceTemplates;
import ru.rassafel.foodsharing.session.util.ButtonsUtil;
import ru.rassafel.foodsharing.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.vkbot.scenarios.SpringCucumberSuperTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.vkbot.Util.*;

public class DeletePlaceScenarioTest extends SpringCucumberSuperTest {
    @Autowired
    PlaceService placeService;

    @Then("user with id {long} choose delete place and types {string}")
    public void user_with_id_choose(Long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST_TO_DELETE,
            PlaceTemplates.buildMapOfPlaces(placeService.findByUserId(userId))));

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), DeleteGeoStep.STEP_INDEX, true);

        assertButtons(response, List.of(ButtonsUtil.BACK_TO_MAIN_MENU, GeoButtonsUtil.BACK_TO_PLACES, GeoButtonsUtil.DELETE_ALL));
    }

    @Then("user with id {long} types {string}")
    public void user_with_id_types(Long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), ChooseOperationGeoStep.STEP_INDEX, true);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES_AFTER_DELETE));

        List<String> geoMainButtons = new ArrayList<>(GeoButtonsUtil.GEO_MAIN_BUTTONS);
        geoMainButtons.add(0, ButtonsUtil.BACK_TO_MAIN_MENU);
        assertButtons(response, geoMainButtons);

        Collection<? extends Place> afterDelete = placeService.findByUserId(userId);

        assertThat(afterDelete)
            .isEmpty();
    }
}
