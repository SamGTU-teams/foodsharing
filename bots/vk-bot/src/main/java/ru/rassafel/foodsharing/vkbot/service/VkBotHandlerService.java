package ru.rassafel.foodsharing.vkbot.service;

import com.vk.api.sdk.objects.callback.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.service.SessionService;
import ru.rassafel.foodsharing.vkbot.model.mapper.VkBotDtoMapper;
import ru.rassafel.foodsharing.vkbot.model.dto.VkUpdate;

@Service
@Slf4j
@RequiredArgsConstructor
public class VkBotHandlerService {
    private final VkBotDtoMapper mapper;
    private final SessionService sessionService;
    private final VkMessenger vkMessenger;
    @Value("${vk.bot.confirm_code}")
    private String confirmCode;

    public String handleUpdate(VkUpdate update) {
        if (update.getType() == Type.MESSAGE_NEW) {
            SessionRequest request = mapper.mapDto(update);
            try {
                SessionResponse response = sessionService.handle(request);
                vkMessenger.send(response);
            } catch (BotException ex) {
                vkMessenger.send(ex.getMessage(), ex.getSendTo().intValue());
            } catch (Exception ex) {
                log.error("Caught an error {}", ex.getMessage());
                vkMessenger.send("Возникла ошибка на сервере, попробуйте повторить позже",
                    request.getFrom().getId().intValue());
            }
        } else if (update.getType() == Type.CONFIRMATION) {
            return confirmCode;
        }
        return "ok";
    }
}
