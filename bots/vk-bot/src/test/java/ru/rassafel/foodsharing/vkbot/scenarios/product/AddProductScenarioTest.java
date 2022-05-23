package ru.rassafel.foodsharing.vkbot.scenarios.product;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rassafel.bot.session.model.dto.From;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.service.SessionEnum;
import ru.rassafel.bot.session.step.product.AddNewProductStep;
import ru.rassafel.bot.session.step.product.ChooseNewProductStep;
import ru.rassafel.bot.session.step.product.ChooseOperationProductStep;
import ru.rassafel.bot.session.templates.ProductTemplates;
import ru.rassafel.bot.session.util.ProductButtonsUtil;
import ru.rassafel.foodsharing.analyzer.controller.stub.ProductAnalyzerControllerStub;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;
import ru.rassafel.foodsharing.vkbot.scenarios.SpringCucumberSuperTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.vkbot.Util.*;

public class AddProductScenarioTest extends SpringCucumberSuperTest {

    @Autowired
    ProductAnalyzerControllerStub productAnalyzerControllerStub;

    @Then("user with id {long} wants to do some with products and types message {string}")
    public void testSelectProductSession(long userId, String message){
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.CHOOSE_PRODUCT_OPERATION));

        List<String> expectedButtons = new ArrayList<>();
        expectedButtons.add("На главную");
        expectedButtons.addAll(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);

        assertButtons(response, expectedButtons);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.PRODUCT.getBeanName(), ChooseOperationProductStep.STEP_INDEX, true);
    }

    @Then("user with id {long} wants to add product and types message {string}")
    public void testSelectAddProductStep(long userId, String message){
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.PRODUCT_NAME_EXPECTATION));

        assertButtons(response, List.of("На главную"));

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.PRODUCT.getBeanName(), ChooseNewProductStep.STEP_INDEX, true);
    }

    @Then("user with id {long} thinking and wants to add {string}")
    public void testPutProductName(long userId,String message){
        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.POSSIBLE_PRODUCT_NAMES));

        ArrayList<String> buttonsList = new ArrayList<>();
        buttonsList.add("На главную");
        buttonsList.add("Попробовать еще");
        buttonsList.addAll(productAnalyzerControllerStub.parseProducts(null, null)
            .stream().map(p -> p.getProduct().getName()).collect(Collectors.toList()));
        assertButtons(response, buttonsList);

        assertUserAndUserSession(getCurrentUser(userId), SessionEnum.PRODUCT.getBeanName(),
            AddNewProductStep.STEP_INDEX, true);
    }

    @Then("user with id {long} get possible product list and choose to add {string}")
    public void testFinallyAddProduct(long userId, String message){
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

        Collection<Product> products = getCurrentUser(userId).getProducts();
        assertThat(products)
            .hasSize(oldProductSize + 1)
            .extracting(p -> p.getName().toLowerCase())
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

        VkUser currentUser = getCurrentUser(userId);

        assertUserAndUserSession(currentUser, SessionEnum.PRODUCT.getBeanName(), ChooseOperationProductStep.STEP_INDEX, true);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS, ProductTemplates.buildMapOfProducts(currentUser.getProducts())));

        int providedSize = currentUser.getProducts().size();

        assertThat(providedSize).isEqualTo(expectedCount);
    }
}