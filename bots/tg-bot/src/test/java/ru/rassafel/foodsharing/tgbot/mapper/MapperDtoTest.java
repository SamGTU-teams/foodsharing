package ru.rassafel.foodsharing.tgbot.mapper;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.tgbot.model.mapper.TgBotDtoMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

public class MapperDtoTest {

    final TgBotDtoMapper botDtoMapper = TgBotDtoMapper.INSTANCE;

    public static List<SessionResponse> testFromResponseToSendMessage() {
        SessionResponse response1 = new SessionResponse();
        response1.setMessage("Message 1");
        response1.setSendTo(new To(12345L));
        response1.setButtons(new BotButtons(List.of("FirstButton", "SecondButton")));

        SessionResponse response2 = new SessionResponse();
        response2.setMessage("Message 2");
        response2.setSendTo(new To(12345L));
        BotButtons botButtons2 = new BotButtons(List.of("FirstButton", "SecondButton"));
        BotButtons.BotButton geo = new BotButtons.BotButton(TgBotDtoMapper.LOCATION_BUTTON_TEXT);
        geo.setGeo(true);
        botButtons2.addButton(geo);
        response2.setButtons(botButtons2);
        return List.of(response1, response2);
    }

    public static List<Update> testFromUpdateToRequest() throws NoSuchFieldException, IllegalAccessException {
        List<Update> updates = new ArrayList<>();
        for (long i = 0; i < 4; i++) {
            Update update = new Update();
            Message message = new Message();
            Chat chat = new Chat();
            Location location = new Location();

            Field messageF = Update.class.getDeclaredField("message");
            messageF.setAccessible(true);

            Field chatF = Message.class.getDeclaredField("chat");
            chatF.setAccessible(true);

            Field locationF = Message.class.getDeclaredField("location");
            locationF.setAccessible(true);

            Field latitudeF = Location.class.getDeclaredField("latitude");
            latitudeF.setAccessible(true);
            Field longitudeF = Location.class.getDeclaredField("longitude");
            longitudeF.setAccessible(true);

            Field textF = Message.class.getDeclaredField("text");
            textF.setAccessible(true);

            Field idF = Chat.class.getDeclaredField("id");
            idF.setAccessible(true);

            idF.set(chat, i);
            chatF.set(message, chat);
            textF.set(message, "Hello bot from " + i);

            latitudeF.set(location, RandomUtils.nextFloat(0f, 150f));
            longitudeF.set(location, RandomUtils.nextFloat(0f, 150f));

            if (i % 2 == 0) {
                locationF.set(message, location);
            }

            messageF.set(update, message);

            updates.add(update);
        }
        return updates;
    }

    @ParameterizedTest
    @MethodSource
    public void testFromUpdateToRequest(Update update) {
        SessionRequest result = botDtoMapper.mapFromUpdate(update);

        assertThat(result.getMessage()).isEqualTo(update.getMessage().getText().toLowerCase().trim());
        ofNullable(update.getMessage().getLocation())
            .ifPresentOrElse(l -> {
                assertThat(result.getLocation().getLat()).isEqualTo(l.getLatitude().doubleValue());
                assertThat(result.getLocation().getLon()).isEqualTo(l.getLongitude().doubleValue());
            }, () -> assertThat(result.getLocation()).isNull());
        assertThat(result.getFrom().getId()).isEqualTo(update.getMessage().getChatId());

    }

    @ParameterizedTest
    @MethodSource
    public void testFromResponseToSendMessage(SessionResponse response) {
        SendMessage result = botDtoMapper.mapToSendMessage(response);
        assertThat(result.getChatId()).isEqualTo(response.getSendTo().getId().toString());
        assertThat(result.getText()).isEqualTo(response.getMessage());
        ReplyKeyboardMarkup replyMarkup = (ReplyKeyboardMarkup) result.getReplyMarkup();
        List<KeyboardRow> keyboard = replyMarkup.getKeyboard();

        assertThat(keyboard).hasSize(response.getButtons().getButtons().size());
        Iterator<BotButtons.BotButton> iterator = response.getButtons().getButtons().iterator();
        for (KeyboardRow keyboardButtons : keyboard) {
            KeyboardButton button = keyboardButtons.get(0);
            BotButtons.BotButton next = iterator.next();
            if (next.isGeo()) {
                assertThat(button.getRequestLocation()).isTrue();
                assertThat(button.getText()).isEqualTo(TgBotDtoMapper.LOCATION_BUTTON_TEXT);
            } else {
                assertThat(button.getText()).isEqualTo(next.getText());
            }
        }
    }
}
