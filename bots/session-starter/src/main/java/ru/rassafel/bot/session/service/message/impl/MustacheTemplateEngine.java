package ru.rassafel.bot.session.service.message.impl;

import com.samskivert.mustache.Mustache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.service.message.TemplateEngine;

@Service
@RequiredArgsConstructor
public class MustacheTemplateEngine implements TemplateEngine {
    private final Mustache.Compiler mustacheEngine;

    @Override
    public String compileTemplate(String template, Object context) {
        return mustacheEngine.compile(template).execute(context);
    }
}
