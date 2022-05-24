package ru.rassafel.foodsharing.vkbot.mapper;

import com.vk.api.sdk.objects.base.Geo;
import com.vk.api.sdk.objects.base.GeoCoordinates;
import com.vk.api.sdk.objects.callback.Type;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.vkbot.model.dto.VkMessage;
import ru.rassafel.foodsharing.vkbot.model.dto.VkUpdate;
import ru.rassafel.foodsharing.vkbot.model.dto.VkUpdateObject;
import ru.rassafel.foodsharing.vkbot.model.mapper.VkBotDtoMapper;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class DtoMapperTest {
    VkBotDtoMapper botDtoMapper = VkBotDtoMapper.INSTANCE;

    public static List<VkUpdate> testMapFromVkObject() {
        Geo geo = new Geo();
        GeoCoordinates coordinates = new GeoCoordinates();
        coordinates.setLongitude(123.2F);
        coordinates.setLatitude(114.0F);
        geo.setCoordinates(coordinates);

        return List.of(
            new VkUpdate(
                Type.CONFIRMATION,
                new VkUpdateObject(
                    new VkMessage(12345, "Привет Я новый пользователЬ", null)
                )
            ),
            new VkUpdate(
                Type.CONFIRMATION,
                new VkUpdateObject(
                    new VkMessage(12345, "Привет Я новый пользователЬ", new Geo())
                )
            ),
            new VkUpdate(
                Type.CONFIRMATION,
                new VkUpdateObject(
                    new VkMessage(12345, "Привет Я новый пользователЬ и шлю сообщение с координатами",
                        geo)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testMapFromVkObject(VkUpdate dto) {
        SessionRequest request = botDtoMapper.mapDto(dto);
        Geo geo = dto.getObject().getMessage().getGeo();
        assertThat(request)
            .hasFieldOrPropertyWithValue("message", dto.getObject().getMessage().getText().toLowerCase(Locale.ROOT))
            .hasFieldOrPropertyWithValue("from.id", (long) dto.getObject().getMessage().getFrom_id());

        if (geo == null || geo.getCoordinates() == null) {
            assertThat(request.getLocation())
                .isNull();
        } else {
            assertThat(request)
                .hasFieldOrPropertyWithValue("location.lat", geo.getCoordinates().getLatitude().doubleValue())
                .hasFieldOrPropertyWithValue("location.lon", geo.getCoordinates().getLongitude().doubleValue());
        }
    }
}
