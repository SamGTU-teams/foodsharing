package ru.rassafel.foodsharing.vkbot.scenarios.place;

import com.github.benmanes.caffeine.cache.Cache;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rassafel.bot.session.model.dto.From;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.SessionEnum;
import ru.rassafel.bot.session.step.geo.ChooseOperationGeoStep;
import ru.rassafel.bot.session.step.geo.EditGeoStep;
import ru.rassafel.bot.session.step.geo.SetNewRadiusGeoStep;
import ru.rassafel.bot.session.templates.PlaceTemplates;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.vkbot.scenarios.SpringCucumberSuperTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.vkbot.Util.*;

public class EditPlaceScenarioTest extends SpringCucumberSuperTest {

    @Autowired
    PlaceService placeService;

    @Autowired
    Cache<Long, Place> geoPointCache;

    @Then("user with id {long} wants to edit place and types {string}")
    public void user_with_id_wants_to_edit_place_and_types(Long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), EditGeoStep.STEP_INDEX, true);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST_TO_EDIT,
            PlaceTemplates.buildMapOfPlaces(placeService.findByUserId(userId))));

        assertButtons(response, List.of("На главную"));
    }

    @Then("user with id {long} types place name {string} he wants to edit")
    public void user_with_id_types_place_name_he_wants_to_edit(Long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), SetNewRadiusGeoStep.STEP_INDEX, true);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_NEW_RADIUS));

        assertButtons(response, List.of("На главную"));

        Place singlePlace = placeService.findByUserId(userId).iterator().next();

        Place cachedPlace = geoPointCache.getIfPresent(userId);
        assertThat(cachedPlace)
            .isNotNull()
            .isEqualTo(singlePlace);
    }
    @Then("user with id {long} types new radius for editable place {string}")
    public void user_with_id_types_new_radius_for_editable_place(Long userId, String message) {

        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), ChooseOperationGeoStep.STEP_INDEX, true);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.PLACE_EDIT_SUCCESS));

        List<String> geoMainButtons = new ArrayList<>(GeoButtonsUtil.GEO_MAIN_BUTTONS);
        geoMainButtons.add(0, "На главную");
        assertButtons(response, geoMainButtons);

        assertThat(geoPointCache.getIfPresent(userId))
            .isNull();

        Place singlePlaceAfterUpdate = placeService.findByUserId(userId).iterator().next();

        assertThat(singlePlaceAfterUpdate.getRadius()).isEqualTo(Integer.parseInt(message));
    }
}
