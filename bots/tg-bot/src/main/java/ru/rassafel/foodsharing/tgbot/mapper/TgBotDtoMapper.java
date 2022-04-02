package ru.rassafel.foodsharing.tgbot.mapper;

import org.mapstruct.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.foodsharing.common.model.PlatformType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Mapper(componentModel = "spring")
public abstract class TgBotDtoMapper {

    @Mappings({
        @Mapping(source = "message", target = "message", ignore = true),
        @Mapping(source = "message.chat.id", target = "from.id"),
        @Mapping(source = "message.from.userName", target = "from.username"),
        @Mapping(source = "message.location.longitude", target = "location.longitude"),
        @Mapping(source = "message.location.latitude", target = "location.latitude"),
    })
    public abstract SessionRequest map(Update update);

    @AfterMapping
    protected void map(Update update, @MappingTarget SessionRequest request){
        final Message message = update.getMessage();
        String text = ofNullable(message.getText()).orElse("#geoPositionRequest");
        request.setMessage(text.toLowerCase().trim());
        request.setType(PlatformType.TG);
    }

    @Mappings({
        @Mapping(source = "message", target = "text")
    })
    public abstract SendMessage map(SessionResponse response);

    @AfterMapping
    protected void map(SessionResponse response, @MappingTarget SendMessage sendMessage){
        sendMessage.setChatId(response.getSendTo().getId());
        List<String> buttons = Optional.ofNullable(response.getButtons()).orElse(ButtonsUtil.DEFAULT_BUTTONS);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(buttons.stream().map(o -> {
            KeyboardRow buttonRow = new KeyboardRow();
            buttonRow.add(o);
            return buttonRow;
        }).collect(Collectors.toList()));
        keyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }
}
