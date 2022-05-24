package ru.rassafel.foodsharing.vkbot.scenarios.place;

import com.github.benmanes.caffeine.cache.Cache;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rassafel.bot.session.model.dto.From;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.service.openmap.AddressService;
import ru.rassafel.bot.session.service.session.SessionEnum;
import ru.rassafel.bot.session.step.geo.AddNewPlaceGeoStep;
import ru.rassafel.bot.session.step.geo.ChooseOperationGeoStep;
import ru.rassafel.bot.session.step.geo.SetNameGeoStep;
import ru.rassafel.bot.session.step.geo.SetRadiusAndFinishSaveGeoStep;
import ru.rassafel.bot.session.templates.PlaceTemplates;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUserPlace;
import ru.rassafel.foodsharing.vkbot.repository.VkUserPlaceRepository;
import ru.rassafel.foodsharing.vkbot.scenarios.SpringCucumberSuperTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.vkbot.Util.*;

public class AddPlaceScenarioTest extends SpringCucumberSuperTest {

    @Autowired
    Cache<Long, Place> geoPointCache;

    @Autowired
    AddressService openStreetMapService;

    @Autowired
    VkUserPlaceRepository placeRepository;

    @When("user with id {long} wants to do some with places and types message {string}")
    public void user_wants_to_add_places_and_types_message(long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.CHOOSE_GEO_OPERATION));

        ArrayList<String> buttons = new ArrayList<>(GeoButtonsUtil.GEO_MAIN_BUTTONS);
        buttons.add(0, ButtonsUtil.BACK_TO_MAIN_MENU);
        assertButtons(response, buttons);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), ChooseOperationGeoStep.STEP_INDEX, true);
    }
    @Then("user with id {long} wants to add a place and send {string}")
    public void user_wants_to_add_a_place_and_send(long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_GEO));

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), AddNewPlaceGeoStep.STEP_INDEX, true);
    }
    @Then("user with id {long} send location with latitude {double} and longitude {double}")
    public void user_send_location_with_latitude_and_longitude(long userId, Double lat, Double lon) {

        SessionRequest request = SessionRequest.builder()
            .location(new GeoPoint(lat, lon))
            .from(From.builder()
                .id(userId)
                .build())
            .message("")
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_GEO_NAME));

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), SetNameGeoStep.STEP_INDEX, true);

        assertButtons(response, List.of(ButtonsUtil.BACK_TO_MAIN_MENU));

        Place fromCache = geoPointCache.getIfPresent(userId);

        assertThat(fromCache)
            .isNotNull()
            .hasFieldOrPropertyWithValue("address", openStreetMapService.getAddress(0d, 0d))
            .hasFieldOrPropertyWithValue("geo.lat", lat)
            .hasFieldOrPropertyWithValue("geo.lon", lon)
            .hasFieldOrPropertyWithValue("userId", userId);

    }
    @Then("user with id {long} thinking and send place name {string}")
    public void user_thinking_and_send_place_name(long userId, String message) {

        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_RADIUS));

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), SetRadiusAndFinishSaveGeoStep.STEP_INDEX, true);

        assertButtons(response, List.of(ButtonsUtil.BACK_TO_MAIN_MENU, GeoButtonsUtil.LEAVE_RADIUS_AS_IS));

        Place fromCache = geoPointCache.getIfPresent(userId);

        assertThat(fromCache)
            .isNotNull()
            .hasFieldOrPropertyWithValue("name", message);
    }
    @Then("user with id {long} send radius {string}")
    public void user_send_radius(long userId, String message) {
        Place notYetSaved = geoPointCache.getIfPresent(userId);
        assertThat(notYetSaved)
            .isNotNull();

        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(PlaceTemplates.FINISHED_SAVE_POINT));

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.GEO.getBeanName(), ChooseOperationGeoStep.STEP_INDEX, true);

        ArrayList<String> buttons = new ArrayList<>(GeoButtonsUtil.GEO_MAIN_BUTTONS);
        buttons.add(0, ButtonsUtil.BACK_TO_MAIN_MENU);
        assertButtons(response, buttons);

        Collection<VkUserPlace> places = placeRepository.findByUserId(userId);

        assertThat(places)
            .hasSize(1);

        VkUserPlace recentlySaved = places.iterator().next();

        assertThat(recentlySaved.getAddress()).isEqualTo(notYetSaved.getAddress());
        assertThat(recentlySaved.getName()).isEqualTo(notYetSaved.getName());
        assertThat(recentlySaved.getUserId()).isEqualTo(userId);
        assertThat(recentlySaved.getGeo()).isEqualTo(notYetSaved.getGeo());
        assertThat(recentlySaved.getRadius()).isEqualTo(notYetSaved.getRadius());

        assertThat(geoPointCache.getIfPresent(userId))
            .isNull();
    }
}
