package ru.rassafel.foodsharing.common.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;
import ru.rassafel.foodsharing.common.model.entity.Region;

/**
 * @author rassafel
 */

@Mapper(componentModel = "spring", uses = {GeoPointEmbeddableMapper.class})
public interface RegionMapper {
    RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

    Region dtoToEntity(RegionDto dto);

    RegionDto entityToDto(Region entity);
}
