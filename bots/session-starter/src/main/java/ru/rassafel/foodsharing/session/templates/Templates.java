package ru.rassafel.foodsharing.session.templates;

public interface Templates {
    String getName();

    default String getName(String raw) {
        return String.format("{{>%s}}", raw);
    }
}
