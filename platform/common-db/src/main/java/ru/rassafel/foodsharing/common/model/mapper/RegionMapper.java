package ru.rassafel.foodsharing.common.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;
import ru.rassafel.foodsharing.common.model.entity.Region;

import java.util.List;
import java.util.Set;

/**
 * @author rassafel
 */

@Mapper(uses = {GeoPointEmbeddableMapper.class})
public interface RegionMapper {
    RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

    Region dtoToEntity(RegionDto dto);

    RegionDto entityToDto(Region entity);

    List<RegionDto> entitiesToDtos(List<Region> dtos);

    Set<RegionDto> entitiesToDtos(Set<Region> dtos);

    List<Region> dtosToEntities(List<RegionDto> regions);

    Set<Region> dtosToEntities(Set<RegionDto> regions);
}
