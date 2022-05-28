Feature: Place session feature
    Scenario: Describes how user can save places
        When new user with id 146072346 prints message Привет бот
        When user with id 146072346 wants to do some with places and types message "места"
        Then user with id 146072346 wants to add a place and send "добавить место"
        Then user with id 146072346 send location with latitude 57.379547 and longitude 162.635706
        Then user with id 146072346 thinking and send place name "дом"
        Then user with id 146072346 send radius "4000"
        Then user with id 146072346 wants to go to main menu and send "на главную"
    Scenario: Describes how user can edit places
        When user with id 146072346 wants to do some with places and types message "места"
        Then user with id 146072346 wants to edit place and types "редактировать место"
        Then user with id 146072346 types place name "дом" he wants to edit
        Then user with id 146072346 types new radius for editable place "4000"
        Then user with id 146072346 wants to go to main menu and send "на главную"
    Scenario: Describes how user can get his places
        When user with id 146072346 wants to do some with places and types message "места"
        Then user with id 146072346 wants to get his places and types "мои места" and expect 1 places
        Then user with id 146072346 wants to go to main menu and send "на главную"
    Scenario: Describes how user can delete places
        When user with id 146072346 wants to do some with places and types message "места"
        Then user with id 146072346 choose delete place and types "удалить место"
        Then user with id 146072346 types "1"
        Then user with id 146072346 wants to go to main menu and send "на главную"



