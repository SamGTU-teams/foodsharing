package ru.rassafel.foodsharing.tgbot.scenarios;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.SessionService;
import ru.rassafel.foodsharing.tgbot.model.domain.TgUser;
import ru.rassafel.foodsharing.tgbot.repository.TgUserRepository;

@CucumberContextConfiguration
@SpringBootTest
public class SpringCucumberSuperTest {
    @Autowired
    public SessionService service;

    @Autowired
    public TemplateEngine templateEngine;

    @Autowired
    public TgUserRepository tgUserRepository;

    public TgUser getCurrentUser(long userId) {
        return tgUserRepository.findWithProductsById(userId).orElseThrow();
    }
}
