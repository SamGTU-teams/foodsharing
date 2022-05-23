package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.interceptor.impl.ExitSessionInterceptor;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.model.mapper.UserDtoMapper;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.templates.MainTemplates;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.bot.session.util.ButtonsUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionFactory factory;
    private final UserService userService;
    private final UserDtoMapper userMapper;
    private final ExitSessionInterceptor interceptor;
    private final WelcomeSessionProducer welcomeSessionProducer;
    private final TemplateEngine templateEngine;

    public SessionResponse handle(SessionRequest request) {

        String userMessage = request.getMessage();

        BotSession botSession;
        User user;

        Optional<? extends User> userOptional = userService.getUser(request.getFrom().getId());

        if (userOptional.isEmpty()) {
            botSession = welcomeSessionProducer.getWelcome();
            user = userMapper.map(request);
            userService.saveUser(user);
        } else {
            user = userOptional.get();
            EmbeddedUserSession userSession = user.getUserSession();

            if (!userSession.isSessionActive()) {
                try {
                    botSession = factory.getSession(userMessage);
                } catch (IllegalArgumentException ex) {
                    throw new BotException(user.getId(),
                        templateEngine.compileTemplate(MainTemplates.INVALID_OPERATION,
                            MainTemplates.buildMapOfOperations(ButtonsUtil.DEFAULT_BUTTONS)));
                }

                EmbeddedUserSession newUserSession = EmbeddedUserSession.builder()
                    .sessionName(botSession.getName())
                    .sessionStep(0)
                    .sessionActive(true)
                    .build();
                user.setUserSession(newUserSession);
            } else {
                botSession = factory.getSessionByName(userSession.getSessionName());
            }
        }
        return interceptor.handle(request, user, botSession);
    }
}
