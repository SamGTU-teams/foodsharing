package ru.rassafel.foodsharing.vkbot.model.mapper;

import com.vk.api.sdk.objects.base.GeoCoordinates;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.mapper.UserDtoMapper;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;
import ru.rassafel.foodsharing.vkbot.model.dto.VkUpdate;

@Mapper
public abstract class VkBotDtoMapper implements UserDtoMapper {
    public static final VkBotDtoMapper INSTANCE = Mappers.getMapper(VkBotDtoMapper.class);

    @Mappings({
        @Mapping(source = "object.message.from_id", target = "from.id"),
        @Mapping(source = "object.message.geo.coordinates", target = "location"),
        @Mapping(target = "from", ignore = true)
    })
    public abstract SessionRequest map(VkUpdate vkUpdate);

    @Mappings({
        @Mapping(source = "latitude", target = "lat"),
        @Mapping(source = "longitude", target = "lon")
    })
    protected abstract GeoPoint map(GeoCoordinates geo);

    @Mappings({
        @Mapping(target = "places", ignore = true),
        @Mapping(source = "from.id", target = "id")
    })
    public abstract VkUser map(SessionRequest rq);


    public SessionRequest mapDto(VkUpdate update) {
        SessionRequest mapped = map(update);
        map(update, mapped);
        return mapped;
    }

    @AfterMapping
    public void map(VkUpdate update, @MappingTarget SessionRequest request) {
        request.setMessage(update.getObject().getMessage().getText().toLowerCase());
    }
}
