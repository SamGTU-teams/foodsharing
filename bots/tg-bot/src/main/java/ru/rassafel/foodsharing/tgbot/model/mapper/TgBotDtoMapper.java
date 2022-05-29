package ru.rassafel.foodsharing.tgbot.model.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.mapper.UserDtoMapper;
import ru.rassafel.foodsharing.tgbot.model.domain.TgUser;

import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Mapper
public abstract class TgBotDtoMapper implements UserDtoMapper {

    public static final String LOCATION_BUTTON_TEXT = "Текущая геолокация";

    public static final TgBotDtoMapper INSTANCE = Mappers.getMapper(TgBotDtoMapper.class);

    @Mappings({
        @Mapping(source = "message", target = "message", ignore = true),
        @Mapping(source = "message.chat.id", target = "from.id"),
        @Mapping(source = "message.location", target = "location"),
        @Mapping(target = "from", ignore = true)
    })
    protected abstract SessionRequest map(Update update);

    @Mappings({
        @Mapping(source = "longitude", target = "lon"),
        @Mapping(source = "latitude", target = "lat")
    })
    protected abstract GeoPoint map(Location location);

    @AfterMapping
    protected void map(Update update, @MappingTarget SessionRequest request) {
        final Message message = update.getMessage();
        String text = ofNullable(message.getText()).map(sms -> sms.toLowerCase().trim()).orElse("");
        request.setMessage(text);
    }

    @Mappings({
        @Mapping(target = "userSession", ignore = true),
        @Mapping(target = "products", ignore = true),
        @Mapping(target = "places", ignore = true),
        @Mapping(source = "from.id", target = "id")
    })
    public abstract TgUser map(SessionRequest request);

    public SessionRequest mapFromUpdate(Update update) {
        SessionRequest mapped = map(update);
        map(update, mapped);
        return mapped;
    }

    @Mappings({
        @Mapping(source = "message", target = "text"),
        @Mapping(target = "chatId", expression = "java(response.getSendTo().getId().toString())")
    })
    public abstract SendMessage map(SessionResponse response);

    public SendMessage mapToSendMessage(SessionResponse response) {
        SendMessage map = map(response);
        mapButtons(response, map);
        return map;
    }

    @AfterMapping
    protected void mapButtons(SessionResponse response, @MappingTarget SendMessage sendMessage) {
        if (response.getButtons() != null) {
            BotButtons buttons = response.getButtons();
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            keyboardMarkup.setKeyboard(buttons.getButtons().stream().map(o -> {
                KeyboardRow buttonRow = new KeyboardRow();
                if (o.isGeo()) {
                    KeyboardButton button = new KeyboardButton();
                    button.setRequestLocation(true);
                    button.setText(LOCATION_BUTTON_TEXT);
                    buttonRow.add(button);
                } else {
                    buttonRow.add(o.getText());
                }
                return buttonRow;
            }).collect(Collectors.toList()));
            keyboardMarkup.setResizeKeyboard(true);

            sendMessage.setReplyMarkup(keyboardMarkup);
        }
    }
}
