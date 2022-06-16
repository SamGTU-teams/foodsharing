package ru.rassafel.foodsharing.session.service.message;

import ru.rassafel.foodsharing.session.service.message.templates.Templates;

/**
 * @author rassafel
 */
public interface TemplateEngine {
    String compileTemplate(Templates template, Object context);

    String compileTemplate(Templates template);

}
