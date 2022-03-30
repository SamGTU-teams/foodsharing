package ru.rassafel.foodsharing.common.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.Product;

import java.util.List;
import java.util.Set;

/**
 * @author rassafel
 */
@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "category", ignore = true)
    Product dtoToEntity(ProductDto dto);

    ProductDto entityToDto(Product entity);

    List<Product> dtosToEntities(List<ProductDto> dtos);

    Set<Product> dtosToEntities(Set<ProductDto> dtos);

    List<ProductDto> entitiesToDtos(List<Product> entities);

    Set<ProductDto> entitiesToDtos(Set<Product> entities);
}
