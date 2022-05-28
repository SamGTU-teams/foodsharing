package ru.rassafel.foodsharing.session.templates;

import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum ProductTemplates implements Templates {
    /**
     * Сообщение пользователю когда он выбирает сессию продуктов
     */
    CHOOSE_PRODUCT_OPERATION("products/choose-product-operation"),
    TOO_MANY_PRODUCTS("products/too-many-products"),
    /**
     * Сообщение по получению всех продуктов пользователя
     */
    LIST_OF_PRODUCTS_TO_DELETE("products/users-product-list-to-delete"),
    LIST_OF_PRODUCTS("products/users-product-list"),
    LIST_OF_PRODUCTS_AFTER_DELETE("products/users-product-list-after-delete"),
    EMPTY_PRODUCTS("products/empty-products"),
    EMPTY_PRODUCTS_AFTER_DELETE("products/empty-products-after-delete"),
    PRODUCT_NAME_EXPECTATION("products/product-name-expectation"),
    PRODUCT_NOT_FOUND("products/product-not-found"),
    POSSIBLE_PRODUCT_NAMES("products/possible-product-names"),
    SUCCESS_ADD_PRODUCT("products/success-add-product"),
    INVALID_PRODUCT_NAME("products/invalid-product-name"),
    SUCCESS_ADD_PRODUCT_AND_QUIT("products/success-add-product-and-quit"),
    TRY_CHOOSE_PRODUCT_MORE("products/try-choose-product-more"),
    BACK_TO_PRODUCTS("products/back-to-products"),
    PRODUCT_NAME_IS_EMPTY("products/product-name-is-empty"),
    PRODUCT_ALREADY_EXISTS("products/product-already-exists");

    private final String name;

    public static Map<String, Object> buildMapOfProducts(Collection<Product> products) {
        AtomicInteger indexer = new AtomicInteger(1);
        return Map.of("products",
            products.stream().map(product ->
                Map.of(
                    "index", indexer.getAndIncrement(),
                    "name", product.getName())).collect(Collectors.toList()));
    }

    @Override
    public String getName() {
        return getName(name);
    }
}
