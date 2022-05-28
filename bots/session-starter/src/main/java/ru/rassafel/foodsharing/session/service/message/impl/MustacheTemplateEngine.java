package ru.rassafel.foodsharing.session.service.message.impl;

import com.samskivert.mustache.Mustache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.templates.Templates;

@Service
@RequiredArgsConstructor
public class MustacheTemplateEngine implements TemplateEngine {
    private final Mustache.Compiler mustacheEngine;

    @Override
    public String compileTemplate(Templates template, Object context) {
        String templateName = String.format("{{>%s}}", template.getName());
        return mustacheEngine.compile(templateName).execute(context);
    }

    @Override
    public String compileTemplate(Templates template) {
        return compileTemplate(template, null);
    }
}
