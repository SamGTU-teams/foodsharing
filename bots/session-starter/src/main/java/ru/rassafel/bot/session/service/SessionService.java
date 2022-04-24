package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.interceptor.ExitSessionInterceptor;
import ru.rassafel.bot.session.mapper.UserDtoMapper;
import ru.rassafel.bot.session.model.entity.user.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.type.BotSession;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionFactory factory;
    private final UserService userService;
    private final UserDtoMapper userMapper;
    private final ExitSessionInterceptor interceptor;
    private final FilePropertiesService messageService;
    private final SessionUtil sessionUtil;

    public SessionResponse handle(SessionRequest request) {

        String userMessage = request.getMessage();

        BotSession botSession;
        User user;

        Optional<User> userOptional = userService.getUser(request.getFrom().getId(), request.getType());

        if (userOptional.isEmpty()) {
            botSession = sessionUtil.getWelcome();
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
                        "На данном этапе доступны только следующие команды\n" +
                            "Продукты\n" +
                            "Места");
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
