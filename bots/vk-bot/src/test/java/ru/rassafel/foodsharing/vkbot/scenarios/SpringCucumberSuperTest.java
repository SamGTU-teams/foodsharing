package ru.rassafel.foodsharing.vkbot.scenarios;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.session.SessionService;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;

@CucumberContextConfiguration
@SpringBootTest
public class SpringCucumberSuperTest {
    @Autowired
    public SessionService service;
    @Autowired
    public TemplateEngine templateEngine;
    @Autowired
    public VkUserRepository vkUserRepository;

    public VkUser getCurrentUser(long userId) {
        return vkUserRepository.findWithProductsById(userId).orElseThrow();
    }
}
