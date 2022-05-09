package ru.rassafel.foodsharing.ibot.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;
import ru.rassafel.foodsharing.common.model.mapper.RegionMapper;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;

import java.util.List;
import java.util.Set;

/**
 * @author rassafel
 */
@Mapper(uses = {ProductMapper.class, RegionMapper.class})
public interface FoodPostMapper {
    FoodPostMapper INSTANCE = Mappers.getMapper(FoodPostMapper.class);

    List<FoodPostDto> entitiesToDtos(List<FoodPost> entities);

    Set<FoodPostDto> entitiesToDtos(Set<FoodPost> entities);

    List<FoodPost> dtosToEntities(List<FoodPostDto> dtos);

    Set<FoodPost> dtosToEntities(Set<FoodPostDto> dtos);

    FoodPostDto entityToDto(FoodPost entity);

    FoodPost dtoToEntity(FoodPostDto dto);
}
