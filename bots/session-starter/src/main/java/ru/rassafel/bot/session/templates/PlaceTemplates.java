package ru.rassafel.bot.session.templates;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlaceTemplates implements Templates {
    ;

    private final String name;

    @Override
    public String getName() {
        return getName(name);
    }
}
