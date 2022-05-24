package ru.rassafel.bot.session.service.message;

import ru.rassafel.bot.session.templates.Templates;

/**
 * @author rassafel
 */
public interface TemplateEngine {
    String compileTemplate(Templates template, Object context);

    String compileTemplate(Templates template);

}
