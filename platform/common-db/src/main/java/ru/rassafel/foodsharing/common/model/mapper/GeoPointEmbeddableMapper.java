package ru.rassafel.foodsharing.common.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.entity.geo.GeoPointEmbeddable;

/**
 * @author rassafel
 */
@Mapper
public interface GeoPointEmbeddableMapper {
    GeoPointEmbeddableMapper INSTANCE = Mappers.getMapper(GeoPointEmbeddableMapper.class);

    GeoPoint entityToDto(GeoPointEmbeddable entity);

    GeoPointEmbeddable dtoToEntity(GeoPoint dto);
}
