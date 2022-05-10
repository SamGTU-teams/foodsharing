package ru.rassafel.foodsharing.vkbot;

import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

    public static void assertVkUserAndUserSession(VkUser user, String sessionName, int step, boolean active) {
        assertThat(user)
            .extracting(VkUser::getUserSession)
            .hasFieldOrPropertyWithValue("sessionName", sessionName)
            .hasFieldOrPropertyWithValue("sessionStep", step)
            .hasFieldOrPropertyWithValue("sessionActive", active);
    }

    public static void assertResponse(SessionResponse response, String message, Long userId) {
        assertThat(response)
            .extracting("message", "sendTo.id")
            .containsExactly(message, userId);
    }

    public static void assertButtons(BotButtons buttons, Map<String, Boolean> buttonsMap) {
        List<BotButtons.BotButton> buttonList = buttons.getButtons();
        Stream<String> textButtons = buttonList.stream().map(BotButtons.BotButton::getText);
        assertThat(textButtons)
            .hasSize(buttonsMap.size())
            .containsExactlyElementsOf(buttonsMap.keySet());

        List<String> providedGeoButtons = buttonList.stream().filter(BotButtons.BotButton::isGeo).map(BotButtons.BotButton::getText).collect(Collectors.toList());
        List<String> expectedGeoButtons = buttonsMap.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toList());

        assertThat(providedGeoButtons)
            .hasSize(expectedGeoButtons.size())
            .containsExactlyInAnyOrderElementsOf(expectedGeoButtons);
    }
}
