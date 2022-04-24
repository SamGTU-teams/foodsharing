package ru.rassafel.foodsharing.vkbot;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.rassafel.bot.session.dto.From;
import ru.rassafel.bot.session.dto.LocationDto;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.place.Place;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.model.entity.user.VkUser;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.SessionService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.common.model.PlatformType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.vkbot.TestUtils.*;

@SpringBootTest
@ActiveProfiles("integration-test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlaceTestSessionVkBotTest {

    private static final Long userId = 1L;
    final SessionRequest request = SessionRequest.builder()
        .from(From.builder()
            .id(userId)
            .username("SomeUsername")
            .build())
        .type(PlatformType.VK)
        .build();
    @Autowired
    Cache<Long, Place> geoCache;
    @Autowired
    SessionService service;
    @Autowired
    UserService userService;
    @Autowired
    PlaceService placeService;

    @Test
    @Order(1)
    public void testSelectPlaceStep() {
        request.setMessage("места");
        SessionResponse response = service.handle(request);
        assertThat(response)
            .extracting("message", "sendTo.id")
            .containsExactly("Выберите:", userId);

        Map<String, Boolean> expectedButtons = new LinkedHashMap<>();
        List<String> geoMainButtonsList = new ArrayList<>(GeoButtonsUtil.GEO_MAIN_BUTTONS);
        geoMainButtonsList.add(0, "На главную");

        geoMainButtonsList.forEach(l -> expectedButtons.put(l, false));

        assertButtons(response.getButtons(), expectedButtons);

        Optional<User> user = userService.getUser(userId, PlatformType.VK);

        assertThat(user)
            .isPresent()
            .get()
            .extracting("id", "userSession.sessionName", "userSession.sessionActive", "userSession.sessionStep")
            .containsExactly(userId, "geoSession", true, 1);
    }

    @Test
    @Order(2)
    public void testCreatePlace() {
        //Select add place step test
        request.setMessage("добавить место");
        SessionResponse response = service.handle(request);
        assertThat(response)
            .extracting("message", "sendTo.id")
            .containsExactly("Отправьте мне точку на карте", userId);


        Map<String, Boolean> expectedButtons = new LinkedHashMap<>();
        expectedButtons.put("На главную", false);
        expectedButtons.put(null, true);
        assertButtons(response.getButtons(), expectedButtons);

        TestUtils.assertVkUserAndUserSession((VkUser) userService.getUser(userId, PlatformType.VK).get(),
            "geoSession", 2, true);

        //Location step test
        LocationDto locationDto = new LocationDto();
        locationDto.setLatitude(122.12f);
        locationDto.setLongitude(12.44f);

        request.setLocation(locationDto);

        SessionResponse locationResponse = service.handle(request);
        Place placeAfterLocation = geoCache.getIfPresent(userId);

        TestUtils.assertResponse(locationResponse, "Теперь дайте название этому месту\n" +
            "Примечание: лучше не использовать цифровые названия", userId);
        assertThat(placeAfterLocation)
            .isNotNull();

        assertButtons(locationResponse.getButtons(), Map.of("На главную", false));

        assertVkUserAndUserSession((VkUser) userService.getUser(userId, PlatformType.VK).get(), "geoSession", 3, true);

        //Set name step test
        request.setMessage("дом");
        SessionResponse setNameResponse = service.handle(request);

        assertResponse(setNameResponse, "Отлично, а теперь укажите радиус поиска вокруг этого места, по умолчанию радиус будет указан в 1 км", userId);

        assertThat(placeAfterLocation)
            .isNotNull()
            .hasFieldOrPropertyWithValue("name", "дом");

        Map<String, Boolean> expectedButtonsAfterSetName = new LinkedHashMap<>();
        expectedButtonsAfterSetName.put("На главную", false);
        expectedButtonsAfterSetName.put("Оставить как есть", false);
        assertButtons(setNameResponse.getButtons(), expectedButtonsAfterSetName);

        //Final test save place
        request.setMessage("123");
        SessionResponse setRadiusResponse = service.handle(request);
        assertResponse(setRadiusResponse, "Круто! Место сохранено", userId);
        assertVkUserAndUserSession((VkUser) userService.getUser(userId, PlatformType.VK).get(), "geoSession",
            1, true);

        Collection<Place> byUserId = placeService.findByUserId(userId, PlatformType.VK);
        assertThat(byUserId)
            .hasSize(1);

        Place resultPlace = byUserId.iterator().next();
        assertThat(resultPlace)
            .extracting("name", "radius")
            .containsExactly("дом", 123);
    }

}
