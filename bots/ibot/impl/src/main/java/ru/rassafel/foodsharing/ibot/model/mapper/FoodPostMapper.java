package ru.rassafel.foodsharing.ibot.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;
import ru.rassafel.foodsharing.common.model.mapper.RegionMapper;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;

import java.util.List;

/**
 * @author rassafel
 */
@Mapper(uses = {ProductMapper.class, RegionMapper.class})
public interface FoodPostMapper {
    FoodPostMapper INSTANCE = Mappers.getMapper(FoodPostMapper.class);

    List<FoodPostDto> entitiesToDtos(List<FoodPost> entities);

    @Mapping(target = "attachments", ignore = true)
    FoodPostDto entityToDto(FoodPost entity);

    @Mapping(target = "id", ignore = true)
    FoodPost dtoToEntity(FoodPostDto dto);
}
