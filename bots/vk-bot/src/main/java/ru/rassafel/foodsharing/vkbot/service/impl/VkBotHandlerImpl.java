package ru.rassafel.foodsharing.vkbot.service.impl;

import com.vk.api.sdk.objects.callback.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.service.Messenger;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.SessionService;
import ru.rassafel.foodsharing.session.service.message.templates.MainTemplates;
import ru.rassafel.foodsharing.vkbot.model.mapper.VkBotDtoMapper;
import ru.rassafel.foodsharing.vkbot.model.vk.VkUpdate;
import ru.rassafel.foodsharing.vkbot.service.VkBotHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class VkBotHandlerImpl implements VkBotHandler {
    private final VkBotDtoMapper mapper;
    private final SessionService sessionService;
    private final Messenger messenger;
    private final TemplateEngine templateEngine;
    @Value("${vk.bot.confirm_code}")
    private String confirmCode;

    @Override
    public String handle(VkUpdate update) {
        if (Type.MESSAGE_NEW.equals(update.getType())) {
            SessionRequest request = mapper.mapDto(update);
            SessionResponse response;
            try {
                response = sessionService.handle(request);
            } catch (BotException ex) {
                response = SessionResponse.builder()
                    .sendTo(new To(ex.getSendTo()))
                    .message(ex.getMessage())
                    .build();
            } catch (Exception ex) {
                log.error("Caught an error while  {}", ex.getMessage());
                response = SessionResponse.builder()
                    .sendTo(new To(request.getFrom().getId()))
                    .message(templateEngine.compileTemplate(MainTemplates.ERROR_ON_SERVER))
                    .build();
            }
            messenger.send(response);
        } else if (Type.CONFIRMATION.equals(update.getType())) {
            return confirmCode;
        }
        return "ok";
    }
}
