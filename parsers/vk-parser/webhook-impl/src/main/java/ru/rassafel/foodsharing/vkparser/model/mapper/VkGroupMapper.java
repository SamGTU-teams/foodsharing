package ru.rassafel.foodsharing.vkparser.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.FullAccessGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.WithoutAccessGroup;

/**
 * @author rassafel
 */
@Mapper
public interface VkGroupMapper {
    VkGroupMapper INSTANCE = Mappers.getMapper(VkGroupMapper.class);

    @Mappings({
        @Mapping(target = "regions", ignore = true),
        @Mapping(target = "confirmationCode", ignore = true),
        @Mapping(target = "serverId", ignore = true)
    })
    VkGroup map(FullAccessGroup source);

    @Mappings({
        @Mapping(target = "regions", ignore = true),
        @Mapping(target = "accessToken", ignore = true),
        @Mapping(target = "serverId", ignore = true)
    })
    VkGroup map(WithoutAccessGroup source);
}
