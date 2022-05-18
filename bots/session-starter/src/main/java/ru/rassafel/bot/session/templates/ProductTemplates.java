package ru.rassafel.bot.session.templates;

import lombok.RequiredArgsConstructor;

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
    LIST_OF_PRODUCTS("products/users-product-list"),
    PRODUCT_NAME_EXPECTATION("products/product-name-expectation"),
    POSSIBLE_PRODUCT_NAMES("products/possible-product-names"),
    SUCCESS_ADD_PRODUCT("products/success-add-product"),
    SUCCESS_ADD_PRODUCT_AND_QUIT("products/success-add-product-and-quit");

    private final String name;
    @Override
    public String getName() {
        return getName(name);
    }
}
