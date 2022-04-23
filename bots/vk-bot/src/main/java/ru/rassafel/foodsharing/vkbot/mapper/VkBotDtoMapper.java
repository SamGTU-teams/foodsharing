package ru.rassafel.foodsharing.vkbot.mapper;

import org.mapstruct.*;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.vkbot.model.VkUpdate;

@Mapper(componentModel = "spring")
public abstract class VkBotDtoMapper {

    @Mappings({
        @Mapping(source = "type", target = "type", ignore = true),
        @Mapping(source = "object.message.from_id", target = "from.id"),
        @Mapping(source = "object.message.geo.coordinates.latitude", target = "location.latitude"),
        @Mapping(source = "object.message.geo.coordinates.longitude", target = "location.longitude"),
    })
    public abstract SessionRequest map(VkUpdate vkUpdate);

    public SessionRequest mapDto(VkUpdate update){
        SessionRequest mapped = map(update);
        map(update, mapped);
        return mapped;
    }

    @AfterMapping
    public void map(VkUpdate update, @MappingTarget SessionRequest request){
        request.setMessage(update.getObject().getMessage().getText().toLowerCase());
        request.setType(PlatformType.VK);
    }
}
