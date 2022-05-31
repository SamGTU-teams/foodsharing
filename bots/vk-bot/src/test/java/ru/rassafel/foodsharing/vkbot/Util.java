package ru.rassafel.foodsharing.vkbot;

import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Util {
    public static void assertButtons(SessionResponse response, List<String> expectedButtons) {
        assertThat(response.getButtons().getButtons())
            .hasSize(expectedButtons.size())
            .flatExtracting(BotButtons.BotButton::getText)
            .containsExactlyElementsOf(expectedButtons);
    }

    public static void assertResponse(SessionRequest request, SessionResponse response, String message) {
        assertThat(response)
            .extracting("message", "sendTo.id")
            .containsExactly(message,
                request.getFrom().getId());
    }

    public static void assertUserAndUserSession(VkUser byId, String sessionName, int step, boolean active) {
        assertThat(byId)
            .extracting(VkUser::getUserSession)
            .hasFieldOrPropertyWithValue("sessionName", sessionName)
            .hasFieldOrPropertyWithValue("sessionStep", step)
            .hasFieldOrPropertyWithValue("sessionActive", active);
    }
}
