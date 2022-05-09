package ru.rassafel.foodsharing.tgbot.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.mapper.UserDtoMapper;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.foodsharing.tgbot.model.TgUser;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Mapper
public abstract class TgBotDtoMapper implements UserDtoMapper {
    public static final TgBotDtoMapper INSTANCE = Mappers.getMapper(TgBotDtoMapper.class);
    @Mappings({
        @Mapping(source = "message", target = "message", ignore = true),
        @Mapping(source = "message.chat.id", target = "from.id"),
        @Mapping(source = "message.from.userName", target = "from.username"),
        @Mapping(source = "message.location.longitude", target = "location.longitude"),
        @Mapping(source = "message.location.latitude", target = "location.latitude"),
    })
    public abstract SessionRequest map(Update update);

    @Mapping(source = "from.id", target = "id")
    public abstract TgUser map(SessionRequest request);

    @AfterMapping
    protected void map(Update update, @MappingTarget SessionRequest request) {
        final Message message = update.getMessage();
        String text = ofNullable(message.getText()).orElse("#geoPositionRequest");
        request.setMessage(text.toLowerCase().trim());
    }

    public SessionRequest mapFromUpdate(Update update) {
        SessionRequest mapped = map(update);
        map(update, mapped);
        return mapped;
    }

    @Mappings({
        @Mapping(source = "message", target = "text")
    })
    public abstract SendMessage map(SessionResponse response);

    @AfterMapping
    protected void map(SessionResponse response, @MappingTarget SendMessage sendMessage) {
        sendMessage.setChatId(response.getSendTo().getId());
        BotButtons buttons = Optional.ofNullable(response.getButtons()).orElse(new BotButtons(ButtonsUtil.DEFAULT_BUTTONS));
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(buttons.getButtons().stream().map(o -> {
            KeyboardRow buttonRow = new KeyboardRow();
            if (o.isGeo()) {
                KeyboardButton button = new KeyboardButton();
                button.setRequestLocation(true);
                button.setText("Геолокация");
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
