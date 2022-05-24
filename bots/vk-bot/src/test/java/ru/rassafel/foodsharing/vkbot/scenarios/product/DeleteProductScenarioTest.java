package ru.rassafel.foodsharing.vkbot.scenarios.product;

import io.cucumber.java.en.Then;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.session.model.dto.From;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.service.session.SessionEnum;
import ru.rassafel.foodsharing.session.step.product.ChooseOperationProductStep;
import ru.rassafel.foodsharing.session.step.product.DeleteProductStep;
import ru.rassafel.foodsharing.session.templates.ProductTemplates;
import ru.rassafel.foodsharing.session.util.ProductButtonsUtil;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;
import ru.rassafel.foodsharing.vkbot.scenarios.SpringCucumberSuperTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.vkbot.Util.*;

public class DeleteProductScenarioTest extends SpringCucumberSuperTest {
    @Then("user with id {long} wants to delete any products and types {string}")
    public void user_wants_to_delete_any_products_and_types(long userId, String message) {

        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(message)
            .build();

        SessionResponse response = service.handle(request);

        VkUser currentUser = getCurrentUser(userId);

        assertUserAndUserSession(currentUser, SessionEnum.PRODUCT.getBeanName(), DeleteProductStep.STEP_INDEX, true);

        assertResponse(request, response, templateEngine.compileTemplate(ProductTemplates.LIST_OF_PRODUCTS_TO_DELETE,
            ProductTemplates.buildMapOfProducts(currentUser.getProducts())));

        assertButtons(response, List.of(ButtonsUtil.BACK_TO_MAIN_MENU, ProductButtonsUtil.DELETE_ALL));
    }

    @Then("user with id {long} wants to delete product and types {string}")
    public void user_wants_to_delete_product_by_number(long userId, String productToDelete) {

        SessionRequest request = SessionRequest.builder()
            .from(From.builder()
                .id(userId)
                .build())
            .message(productToDelete)
            .build();

        SessionResponse response = service.handle(request);

        VkUser afterDeleteUser = getCurrentUser(userId);

        assertUserAndUserSession(afterDeleteUser, SessionEnum.PRODUCT.getBeanName(), ChooseOperationProductStep.STEP_INDEX, true);

        Collection<Product> productsAfterDelete = afterDeleteUser.getProducts();

        assertThat(productsAfterDelete.size()).isEqualTo(0);

        String expectedMessage = templateEngine.compileTemplate(ProductTemplates.EMPTY_PRODUCTS_AFTER_DELETE);

        assertResponse(request, response, expectedMessage);

        List<String> productMainButtons = new ArrayList<>(ProductButtonsUtil.PRODUCT_MAIN_BUTTONS);
        productMainButtons.add(0, ButtonsUtil.BACK_TO_MAIN_MENU);

        assertButtons(response, productMainButtons);
    }
}
