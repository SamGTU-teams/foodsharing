package ru.rassafel.foodsharing.vkbot;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.rassafel.bot.session.dto.From;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.model.entity.user.VkUser;
import ru.rassafel.bot.session.repository.user.VkUserRepository;
import ru.rassafel.bot.session.service.FilePropertiesService;
import ru.rassafel.bot.session.service.ProductService;
import ru.rassafel.bot.session.service.SessionEnum;
import ru.rassafel.bot.session.service.SessionService;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.bot.session.util.ProductButtonsUtil;
import ru.rassafel.foodsharing.common.model.PlatformType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("integration-test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductTestSessionVkBot {
    final Long userId = 12345L;

    final SessionRequest request = SessionRequest.builder()
        .from(From.builder()
            .id(userId)
            .username("SomeUsername")
            .build())
        .type(PlatformType.VK)
        .build();

    @MockBean
    ProductService productService;
    @Autowired
    SessionService service;
    @Autowired
    FilePropertiesService messagesService;
    @Autowired
    VkUserRepository vkUserRepository;
    @Autowired
    JdbcTemplate jdbc;

    private static void assertResponse(SessionRequest request, SessionResponse response, String message) {
        assertThat(response)
            .extracting("message", "sendTo.id")
            .containsExactly(message,
                request.getFrom().getId());
    }

    private static void assertButtons(SessionResponse response, List<String> expectedButtons) {
        assertThat(response.getButtons().getButtons())
            .hasSize(expectedButtons.size())
            .flatExtracting(BotButtons.BotButton::getText)
            .containsExactlyElementsOf(expectedButtons);
    }

    @Test
    @Order(1)
    void testNewUser() {

        request.setMessage("Привет!!!");

        SessionResponse response = service.handle(request);
        assertResponse(request, response, messagesService.getSessionMessage("welcome"));
        assertButtons(response, ButtonsUtil.DEFAULT_BUTTONS);

        assertThat(vkUserRepository.findById(userId))
            .isPresent();
    }

    @Test
    @Order(2)
    void testProductSessionWithInvalidMessage() {
        request.setMessage("Хочу продукты");

        assertThatThrownBy(() -> service.handle(request))
            .isInstanceOf(BotException.class)
            .hasFieldOrPropertyWithValue("message", "На данном этапе доступны только следующие команды\n" +
                "Продукты\n" +
                "Места")
            .hasFieldOrPropertyWithValue("sendTo", userId);
    }

    @Test
    @Order(3)
    void testStartProductSession() {
        request.setMessage("продукты");

        SessionResponse response = service.handle(request);

        assertResponse(request, response, "Выберите:");

        List<String> expectedButtons = new ArrayList<>();
        expectedButtons.add("На главную");
        expectedButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);

        assertButtons(response, expectedButtons);

        assertUserAndUserSessionAndGetUser(SessionEnum.PRODUCT.getBeanName(), 1, true);
    }

    @Test
    @Order(4)
    void testAddProduct() {
        request.setMessage("добавить продукт");

        SessionResponse response = service.handle(request);

        assertResponse(request, response, "Введите название продукта, который хотите добавить");

        assertButtons(response, List.of("На главную"));

        assertUserAndUserSessionAndGetUser(SessionEnum.PRODUCT.getBeanName(), 2, true);

        request.setMessage("Яблоко");

        when(productService.getSimilarProducts(any())).thenReturn(List.of("Кефир"));

        SessionResponse appleResponse = service.handle(request);

        assertResponse(request, appleResponse, "Возможно вы имели ввиду следующие продукты, выберите какой из них вы хотите добавить или попробуйте ввести еще");

        assertUserAndUserSessionAndGetUser(SessionEnum.PRODUCT.getBeanName(), 3, true);

        assertButtons(appleResponse, List.of("На главную", "Попробовать еще", "Кефир"));

        request.setMessage("кефир");

        SessionResponse finishedResponse = service.handle(request);

        assertResponse(request, finishedResponse, "Вы успешно добавили продукт!\nВведите еще");

        assertButtons(finishedResponse, List.of("На главную"));

        assertUserAndUserSessionAndGetUser(SessionEnum.PRODUCT.getBeanName(), 2, true);

        List<Map<String, Object>> usersProducts = jdbc.queryForList("select * from product p join vk_bot.vk_user_products up on p.id = up.product_id where up.user_id = ?",
            userId);
        assertThat(usersProducts)
            .hasSize(1);
        Map<String, Object> firstProduct = usersProducts.iterator().next();
        assertThat(firstProduct)
            .containsEntry("name", "Кефир");

    }

    private User assertUserAndUserSessionAndGetUser(String sessionName, int step, boolean active) {
        Optional<VkUser> byId = vkUserRepository.findById(userId);
        assertThat(byId)
            .isPresent()
            .get()
            .extracting(VkUser::getUserSession)
            .hasFieldOrPropertyWithValue("sessionName", sessionName)
            .hasFieldOrPropertyWithValue("sessionStep", step)
            .hasFieldOrPropertyWithValue("sessionActive", active);
        return byId.get();
    }
}
