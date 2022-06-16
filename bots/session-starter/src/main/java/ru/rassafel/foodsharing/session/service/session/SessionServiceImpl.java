package ru.rassafel.foodsharing.session.service.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.interceptor.impl.ExitSessionInterceptor;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.model.mapper.UserDtoMapper;
import ru.rassafel.foodsharing.session.service.SessionService;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.message.templates.MainTemplates;
import ru.rassafel.foodsharing.session.service.session.type.BotSession;
import ru.rassafel.foodsharing.session.util.button.ButtonsUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionFactory factory;
    private final UserService userService;
    private final UserDtoMapper userMapper;
    private final ExitSessionInterceptor interceptor;
    private final WelcomeSessionProducer welcomeSessionProducer;
    private final TemplateEngine templateEngine;

    @Override
    public SessionResponse handle(SessionRequest request) {
        String userMessage = request.getMessage();

        BotSession botSession;
        User user;
        Long userId = request.getFrom().getId();

        Optional<? extends User> userOptional = userService.getUser(userId);

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
                    throw new BotException(userId,
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
