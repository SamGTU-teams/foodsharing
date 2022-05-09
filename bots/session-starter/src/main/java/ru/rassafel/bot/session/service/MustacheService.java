package ru.rassafel.bot.session.service;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.TemplateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MustacheService {

    public static String GET_PLACES_TEMPLATE = "templates/places.mustache";

    private final MustacheFactory factory;

//    public String buildPlacesTemplate(String templateName, ){
//        Mustache compile = factory.compile(templateName);
//
//    }

}
