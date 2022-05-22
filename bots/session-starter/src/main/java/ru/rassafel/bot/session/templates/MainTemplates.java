package ru.rassafel.bot.session.templates;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MainTemplates implements Templates {

    WELCOME("welcome"),
    INVALID_OPERATION("invalid-operation-name"),
    BACK_TO_MAIN("back-to-main");

    private final String name;

    @Override
    public String getName(){
        return getName(name);
    }

}
