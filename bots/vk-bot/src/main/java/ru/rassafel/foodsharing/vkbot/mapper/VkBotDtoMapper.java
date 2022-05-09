package ru.rassafel.foodsharing.vkbot.mapper;

import org.mapstruct.*;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.mapper.UserDtoMapper;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;
import ru.rassafel.foodsharing.vkbot.model.dto.VkUpdate;

@Mapper(componentModel = "spring")
public abstract class VkBotDtoMapper implements UserDtoMapper {

    @Mappings({
        @Mapping(source = "type", target = "type", ignore = true),
        @Mapping(source = "object.message.from_id", target = "from.id"),
        @Mapping(source = "object.message.geo.coordinates.latitude", target = "location.latitude"),
        @Mapping(source = "object.message.geo.coordinates.longitude", target = "location.longitude"),
    })
    public abstract SessionRequest map(VkUpdate vkUpdate);

    @Mapping(source = "from.id", target = "id")
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
