package ru.rassafel.bot.session.service.message.impl;

import com.samskivert.mustache.Mustache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.templates.Templates;

@Service
@RequiredArgsConstructor
public class MustacheTemplateEngine implements TemplateEngine {
    private final Mustache.Compiler mustacheEngine;

    @Override
    public String compileTemplate(Templates template, Object context) {
        return mustacheEngine.compile(template.getName()).execute(context);
    }

    @Override
    public String compileTemplate(Templates template) {
        return compileTemplate(template, null);
    }
}
