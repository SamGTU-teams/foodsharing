package ru.rassafel.foodsharing.tgbot.mapper;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.bot.session.model.dto.SessionRequest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

public class MapperDtoTest {

    TgBotDtoMapper botDtoMapper = TgBotDtoMapper.INSTANCE;

    @ParameterizedTest
    @MethodSource
    public void testMapper(Update update) {
        SessionRequest result = botDtoMapper.mapFromUpdate(update);

        assertThat(result.getMessage()).isEqualTo(update.getMessage().getText().toLowerCase().trim());
        ofNullable(update.getMessage().getLocation())
            .ifPresentOrElse(l -> {
                assertThat(result.getLocation().getLat()).isEqualTo(l.getLatitude().doubleValue());
                assertThat(result.getLocation().getLon()).isEqualTo(l.getLongitude().doubleValue());
            }, () -> assertThat(result.getLocation()).isNull());
        assertThat(result.getFrom().getId()).isEqualTo(update.getMessage().getChatId());

    }

    public static List<Update> testMapper() throws NoSuchFieldException, IllegalAccessException {
        List<Update> updates = new ArrayList<>();
        for (Integer i = 0; i < 4; i++) {
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

            idF.set(chat, i.longValue());
            chatF.set(message, chat);
            textF.set(message, "Hello bot from " + i);

            latitudeF.set(location, RandomUtils.nextFloat(0F, 150F));
            longitudeF.set(location, RandomUtils.nextFloat(0F, 150F));

            if(i % 2 == 0) {
                locationF.set(message, location);
            }

            messageF.set(update, message);

            updates.add(update);
        }
        return updates;
    }

}
