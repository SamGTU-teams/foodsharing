package ru.rassafel.bot.session.templates;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlaceTemplates implements Templates {
    CHOOSE_GEO_OPERATION("places/choose-geo-operation"),
    EXPECTATION_OF_GEO("places/expectation-of-geo");

    private final String name;

    @Override
    public String getName() {
        return getName(name);
    }
}
