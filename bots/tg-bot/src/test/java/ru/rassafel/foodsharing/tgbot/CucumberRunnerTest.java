package ru.rassafel.foodsharing.tgbot;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",
    glue = "ru.rassafel.foodsharing.tgbot.scenarios")
public class CucumberRunnerTest {
}
