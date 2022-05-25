package ru.rassafel.foodsharing.vkbot.scenarios;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ru.rassafel.foodsharing.session.model.dto.From;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.templates.MainTemplates;
import ru.rassafel.foodsharing.session.util.ButtonsUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.vkbot.Util.assertButtons;
import static ru.rassafel.foodsharing.vkbot.Util.assertResponse;

public class CommonUserScenarioTest extends SpringCucumberSuperTest {
    @When("^new user with id (\\d+) prints message (.+)$")
    public void testFirstContact(Long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse rs = service.handle(request);

        assertResponse(request, rs, templateEngine.compileTemplate(MainTemplates.WELCOME));
        assertButtons(rs, ButtonsUtil.DEFAULT_BUTTONS);

        assertThat(vkUserRepository.findById(userId))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", userId)
            .hasFieldOrPropertyWithValue("userSession.sessionName", "")
            .hasFieldOrPropertyWithValue("userSession.sessionStep", 0)
            .hasFieldOrPropertyWithValue("userSession.sessionActive", false);
    }

    @Then("user with id {long} wants to go to main menu and send {string}")
    public void testGoToMainMenu(long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);
        assertResponse(request, response, templateEngine.compileTemplate(MainTemplates.BACK_TO_MAIN));

        assertThat(getCurrentUser(userId).getUserSession())
            .hasFieldOrPropertyWithValue("sessionActive", false);

        assertButtons(response, ButtonsUtil.DEFAULT_BUTTONS);
    }
}
