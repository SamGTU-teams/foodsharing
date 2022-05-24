package ru.rassafel.foodsharing.vkbot.scenarios.place;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rassafel.bot.session.model.dto.From;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.session.SessionEnum;
import ru.rassafel.bot.session.step.geo.ChooseOperationGeoStep;
import ru.rassafel.bot.session.step.geo.DeleteGeoStep;
import ru.rassafel.bot.session.templates.PlaceTemplates;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
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

        assertButtons(response, List.of("На главную", "Удалить все"));
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
        geoMainButtons.add(0, "На главную");
        assertButtons(response, geoMainButtons);

        Collection<Place> afterDelete = placeService.findByUserId(userId);

        assertThat(afterDelete)
            .isEmpty();
    }
}
