package ru.rassafel.foodsharing.vkbot.service;

import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.foodsharing.common.repository.user.VkUserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VkMessengerService {

    private final VkApiClient vk;
    private final GroupActor groupActor;
    private final AtomicReference<LocalDateTime> lastExecuted = new AtomicReference<>();

    public void sendMessage(SessionResponse response) {
        try {
            MessagesSendQuery messagesSendQuery = vk.messages().send(groupActor)
                .message(response.getMessage()).userId(response.getSendTo().getId().intValue())
                .randomId(new Random().nextInt(500_000))
                .keyboard(createKeyboard(response.getButtons()));
            messagesSendQuery
                .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, Long id) {
        try {
            MessagesSendQuery messagesSendQuery = vk.messages().send(groupActor)
                .message(message)
                .userId(id.intValue())
                .randomId(new Random().nextInt(500_000));
            messagesSendQuery
                .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, List<Integer> ids) {
        try {
            MessagesSendQuery messagesSendQuery = vk.messages()
                .send(groupActor)
                .message(message)
                .userIds(ids)
                .randomId(new Random().nextInt(500_000));
            messagesSendQuery
                .executeAsString();
            System.out.println();
        } catch (ClientException e) {
            e.printStackTrace();
        }
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
