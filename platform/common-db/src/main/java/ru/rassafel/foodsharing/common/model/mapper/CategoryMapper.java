package ru.rassafel.foodsharing.common.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.dto.CategoryDto;
import ru.rassafel.foodsharing.common.model.entity.Category;

/**
 * @author rassafel
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category dtoToEntity(CategoryDto dto);

    CategoryDto entityToDto(Category entity);
}
