package ru.rassafel.foodsharing.vkbot.service.impl;

import com.vk.api.sdk.client.AbstractQueryBuilder;
import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.service.Messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
@Slf4j
public class VkMessenger implements Messenger {
    public static final KeyboardButtonAction GEO_ACTION = new KeyboardButtonAction()
        .setType(TemplateActionTypeNames.LOCATION);
    private final VkApiClient vk;
    private final GroupActor groupActor;

    @Override
    public void send(List<SessionResponse> responses) {
        List<AbstractQueryBuilder> queries = responses.stream()
            .map(this::createQuery)
            .collect(Collectors.toList());
        try {
            ClientResponse clientRs = vk.execute()
                .batch(groupActor, queries)
                .executeAsRaw();
            ofNullable(clientRs)
                .ifPresent(rs -> {
                    log.debug("Sent event to VK with response code : {} and body : {}",
                        rs.getStatusCode(), rs.getContent());
                });
        } catch (
            ClientException e) {
            log.error("Caught an exception while sending message with error message : {}", e.getMessage());
        }
    }

    @Override
    public void send(SessionResponse response) {
        send(List.of(response));
    }

    private MessagesSendQuery createQuery(SessionResponse response) {
        To sendTo = response.getSendTo();
        List<Integer> ids = ofNullable(sendTo.getId())
            .map(List::of)
            .orElse(sendTo.getUserIds())
            .stream()
            .map(Long::intValue)
            .collect(Collectors.toList());

        MessagesSendQuery sendQuery = vk.messages().send(groupActor)
            .message(response.getMessage())
            .userIds(ids)
            .randomId(new Random().nextInt(500_000))
            .dontParseLinks(false);

        BotButtons buttons = response.getButtons();
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
                button.setAction(GEO_ACTION);
            } else {
                button.setAction(new KeyboardButtonAction()
                        .setLabel(rawKey.getText())
                        .setType(TemplateActionTypeNames.TEXT))
                    .setColor(KeyboardButtonColor.POSITIVE);
            }
            List<KeyboardButton> line = List.of(button);
            keyboardButtons.add(line);
        }
        return new Keyboard().setButtons(keyboardButtons);
    }
}
