package ru.rassafel.foodsharing.vkparser.model.mapper;

import org.mapstruct.Mapper;
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

    VkGroup map(FullAccessGroup source);

    VkGroup map(WithoutAccessGroup source);
}
