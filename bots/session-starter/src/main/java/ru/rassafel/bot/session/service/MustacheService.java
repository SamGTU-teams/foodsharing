package ru.rassafel.bot.session.service;

import com.github.mustachejava.MustacheFactory;
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
