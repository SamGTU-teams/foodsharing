package ru.rassafel.foodsharing.common.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.Product;

/**
 * @author rassafel
 */
@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product dtoToEntity(ProductDto dto);

    ProductDto entityToDto(Product entity);
}
