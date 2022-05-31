package ru.rassafel.foodsharing.vkbot;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",
    glue = "ru.rassafel.foodsharing.vkbot.scenarios")
public class CucumberRunnerTest {
}
