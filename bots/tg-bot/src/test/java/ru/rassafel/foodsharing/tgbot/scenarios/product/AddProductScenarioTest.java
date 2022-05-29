package ru.rassafel.foodsharing.tgbot.scenarios.product;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rassafel.foodsharing.analyzer.controller.stub.ProductAnalyzerControllerStub;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.session.model.dto.From;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.service.session.SessionEnum;
import ru.rassafel.foodsharing.session.service.session.step.product.AddNewProductStep;
import ru.rassafel.foodsharing.session.service.session.step.product.ChooseNewProductStep;
import ru.rassafel.foodsharing.session.service.session.step.product.ChooseOperationProductStep;
import ru.rassafel.foodsharing.session.service.message.templates.ProductTemplates;
import ru.rassafel.foodsharing.session.util.button.ButtonsUtil;
import ru.rassafel.foodsharing.session.util.button.ProductButtonsUtil;
import ru.rassafel.foodsharing.tgbot.model.domain.TgUser;
import ru.rassafel.foodsharing.tgbot.scenarios.SpringCucumberSuperTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.tgbot.Util.*;

public class AddProductScenarioTest extends SpringCucumberSuperTest {

    @Autowired
    ProductAnalyzerControllerStub productAnalyzerControllerStub;

    @Then("user with id {long} wants to do some with products and types message {string}")
    public void testSelectProductSession(long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.CHOOSE_PRODUCT_OPERATION));

        List<String> expectedButtons = new ArrayList<>();
        expectedButtons.add(ButtonsUtil.BACK_TO_MAIN_MENU);
        expectedButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);

        assertButtons(response, expectedButtons);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.PRODUCT.getBeanName(), ChooseOperationProductStep.STEP_INDEX, true);
    }

    @Then("user with id {long} wants to add product and types message {string}")
    public void testSelectAddProductStep(long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.PRODUCT_NAME_EXPECTATION));

        assertButtons(response, List.of(ButtonsUtil.BACK_TO_MAIN_MENU, ProductButtonsUtil.BACK_TO_PRODUCTS));

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.PRODUCT.getBeanName(), ChooseNewProductStep.STEP_INDEX, true);
    }

    @Then("user with id {long} thinking and wants to add {string}")
    public void testPutProductName(long userId, String message) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.POSSIBLE_PRODUCT_NAMES));

        ArrayList<String> buttonsList = new ArrayList<>();
        buttonsList.add(ButtonsUtil.BACK_TO_MAIN_MENU);
        buttonsList.add(ProductButtonsUtil.BACK_TO_PRODUCTS);
        buttonsList.add(ProductButtonsUtil.TRY_MORE);
        buttonsList.addAll(productAnalyzerControllerStub.parseProducts(null, null)
            .stream().map(p -> p.getProduct().getName()).collect(Collectors.toList()));
        assertButtons(response, buttonsList);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.PRODUCT.getBeanName(),
            AddNewProductStep.STEP_INDEX, true);
    }

    @Then("user with id {long} get possible product list and choose to add {string}")
    public void testFinallyAddProduct(long userId, String message) {
        int oldProductSize = getCurrentUser(userId).getProducts().size();
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.SUCCESS_ADD_PRODUCT));

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.PRODUCT.getBeanName(),
            ChooseNewProductStep.STEP_INDEX, true);

        assertButtons(response, List.of(ButtonsUtil.BACK_TO_MAIN_MENU, ProductButtonsUtil.BACK_TO_PRODUCTS));

        Collection<Product> products = getCurrentUser(userId).getProducts();
        assertThat(products)
            .hasSize(oldProductSize + 1)
            .extracting(p -> p.getName().toLowerCase(Locale.ROOT))
            .containsExactly(message);
    }


    @Then("user with id {long} wants to get own products and types {string} and he want to see {int} products")
    public void user_wants_to_get_own_products_and_types(long userId, String message, Integer expectedCount) {
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        TgUser currentUser = getCurrentUser(userId);

        assertUserAndUserSession(currentUser, SessionEnum.PRODUCT.getBeanName(), ChooseOperationProductStep.STEP_INDEX, true);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS, ProductTemplates.buildMapOfProducts(currentUser.getProducts())));

        int providedSize = currentUser.getProducts().size();

        assertThat(providedSize).isEqualTo(expectedCount);

        List<String> productMainButtons = new ArrayList<>(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);
        productMainButtons.add(0, ButtonsUtil.BACK_TO_MAIN_MENU);
        assertButtons(response, productMainButtons);
    }
}
