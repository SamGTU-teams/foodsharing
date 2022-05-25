package ru.rassafel.foodsharing.vkbot.service;

import com.vk.api.sdk.objects.callback.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.session.SessionService;
import ru.rassafel.foodsharing.session.templates.MainTemplates;
import ru.rassafel.foodsharing.vkbot.model.dto.VkUpdate;
import ru.rassafel.foodsharing.vkbot.model.mapper.VkBotDtoMapper;

import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class VkBotHandlerService {
    private final VkBotDtoMapper mapper;
    private final SessionService sessionService;
    @Value("${vk.bot.confirm_code}")
    private String confirmCode;
    private final BlockingQueue<SessionResponse> queue;
    private final TemplateEngine templateEngine;

    public String handleUpdate(VkUpdate update) {
        if (update.getType() == Type.MESSAGE_NEW) {
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
            try {
                queue.put(response);
            } catch (InterruptedException e) {
                log.error("Exception while put response to queue with message : {}",
                    e.getMessage());
            }
        } else if (update.getType() == Type.CONFIRMATION) {
            return confirmCode;
        }
        return "ok";
    }
}
