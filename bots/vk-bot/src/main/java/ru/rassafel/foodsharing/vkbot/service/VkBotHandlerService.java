package ru.rassafel.foodsharing.vkbot.service;

import com.vk.api.sdk.objects.callback.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.service.SessionService;
import ru.rassafel.foodsharing.vkbot.mapper.VkBotDtoMapper;
import ru.rassafel.foodsharing.vkbot.model.VkUpdate;

@Service
@Slf4j
@RequiredArgsConstructor
public class VkBotHandlerService {

    private final VkBotDtoMapper mapper;
    private final SessionService sessionService;
    private final VkMessengerService vkMessengerService;
    @Value("${vk.server.confirm_code}")
    private String confirmCode;

    public String handleUpdate(VkUpdate update) {
        if (update.getType() == Type.MESSAGE_NEW) {
            SessionRequest request = mapper.map(update);
            try {
                SessionResponse response = sessionService.handle(request);
                vkMessengerService.sendMessage(response);
            } catch (BotException ex){
                vkMessengerService.sendMessage(ex.getMessage(), ex.getSendTo());
            }catch (Exception ex) {

                log.error("Caught an error {}", ex.getMessage());
                ex.printStackTrace();
            }
        } else if (update.getType() == Type.CONFIRMATION) {
            return confirmCode;
        }
        return "ok";
    }

}
