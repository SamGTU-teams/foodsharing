package ru.rassafel.foodsharing.analyzer.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.analyzer.model.dto.ScoreProductDto;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;

/**
 * @author rassafel
 */
@Mapper(uses = ProductMapper.class)
public interface ScoreProductMapper {
    ScoreProductMapper INSTANCE = Mappers.getMapper(ScoreProductMapper.class);

    ScoreProduct dtoToEntity(ScoreProductDto dto);

    ScoreProductDto entityToDto(ScoreProduct entity);
}
