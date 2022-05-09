package ru.rassafel.foodsharing.vkbot.service;

import com.vk.api.sdk.client.AbstractQueryBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.dto.BotButtons;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class VkMessageSchedulerService {

    private final VkApiClient vk;
    private final GroupActor groupActor;
    private final BlockingQueue<AbstractQueryBuilder> queue;

    @PostConstruct
    public void scheduleEvent() {
        IntStream.range(0, 1000).forEach(o -> queue.add(createQuery("sss " + o, null, 146072345)));
    }

    public void scheduleEvent(SessionResponse response) {
        MessagesSendQuery query =
            createQuery(response.getMessage(),
                response.getButtons(),
                response.getSendTo().getId().intValue());
        try {
            queue.put(query);
        } catch (InterruptedException e) {
            log.error("Caught an exception with message : {}", e.getMessage());
        }
    }

    public void scheduleEvent(String message, Integer... ids) {
        MessagesSendQuery query = createQuery(message, null, ids);
        try {
            queue.put(query);
        } catch (InterruptedException e) {
            log.error("Caught an exception with message : {}", e.getMessage());
        }
    }

    private MessagesSendQuery createQuery(String message, BotButtons buttons, Integer... ids) {
        MessagesSendQuery sendQuery = vk.messages().send(groupActor)
            .message(message)
            .userIds(ids)
            .randomId(new Random().nextInt(500_000));
        if (buttons != null && buttons.getButtons() != null && !buttons.getButtons().isEmpty()) {
            sendQuery.keyboard(createKeyboard(buttons));
        }
        return sendQuery;
    }

    private Keyboard createKeyboard(BotButtons rawKeys) {
        List<List<KeyboardButton>> keyboardButtons = new ArrayList<>();
        for (BotButtons.BotButton rawKey : rawKeys.getButtons()) {
            KeyboardButton button = new KeyboardButton();
            if (rawKey.isGeo()) {
                button.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.LOCATION));
            } else {
                button.setAction(new KeyboardButtonAction()
                        .setLabel(rawKey.getText())
                        .setType(TemplateActionTypeNames.TEXT))
                    .setColor(KeyboardButtonColor.POSITIVE);
            }
            List<KeyboardButton> line = List.of(button);
            keyboardButtons.add(line);
        }
        Keyboard keyboard = new Keyboard();
        keyboard.setButtons(keyboardButtons);
        return keyboard;
    }

}
