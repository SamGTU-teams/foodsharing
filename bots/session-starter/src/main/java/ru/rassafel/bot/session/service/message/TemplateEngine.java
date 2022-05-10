package ru.rassafel.bot.session.service.message;

/**
 * @author rassafel
 */
public interface TemplateEngine {
    String compileTemplate(String template, Object context);
}
