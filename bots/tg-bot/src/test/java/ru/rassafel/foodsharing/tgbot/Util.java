package ru.rassafel.foodsharing.tgbot;

import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.tgbot.model.TgUser;

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

    public static void assertUserAndUserSession(TgUser byId, String sessionName, int step, boolean active) {
        assertThat(byId)
            .extracting(TgUser::getUserSession)
            .hasFieldOrPropertyWithValue("sessionName", sessionName)
            .hasFieldOrPropertyWithValue("sessionStep", step)
            .hasFieldOrPropertyWithValue("sessionActive", active);
    }

}
