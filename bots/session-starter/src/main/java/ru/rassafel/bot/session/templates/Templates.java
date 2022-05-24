package ru.rassafel.bot.session.templates;

public interface Templates {
    String getName();

    default String getName(String raw) {
        return String.format("{{>%s}}", raw);
    }
}
