package ru.rassafel.bot.session.service.message.impl;

import com.samskivert.mustache.Mustache;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rassafel
 */
class MustacheTemplateEngineTest {
    @Test
    void test() {
        Mustache.Compiler compiler = Mustache.compiler()
            .withLoader(name -> {
                URL resource = getClass().getClassLoader().getResource("templates/" + name + ".mustache");
                return Files.newBufferedReader(Path.of(resource.toURI()));
            });

        String execute = compiler.compile("Text {{>test}}").execute(Map.of("key", "test value",
            "secondKey", "new test value"));

        System.out.println(execute);
    }

}
