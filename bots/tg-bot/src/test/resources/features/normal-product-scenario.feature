Feature: Product session feature
    Scenario: Describes how user can add products
        When new user with id 12345 prints message Привет бот
        Then user with id 12345 wants to do some with products and types message "продукты"
        Then user with id 12345 wants to add product and types message "добавить продукт"
        Then user with id 12345 thinking and wants to add "молоко"
        Then user with id 12345 get possible product list and choose to add "молоко"
        Then user with id 12345 wants to go to main menu and send "на главную"
    Scenario: Describes how user can get products
        When user with id 12345 wants to do some with products and types message "продукты"
        Then user with id 12345 wants to get own products and types "мои продукты" and he want to see 1 products
    Scenario: Describes how user can delete products
        Then user with id 12345 wants to delete any products and types "удалить продукт"
        Then user with id 12345 wants to delete product and types "1"
