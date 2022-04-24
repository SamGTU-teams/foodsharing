package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.ModelFactory;
import ru.rassafel.foodsharing.common.model.dto.CategoryDto;
import ru.rassafel.foodsharing.common.model.entity.product.Category;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.common.model.ModelFactory.*;

/**
 * @author rassafel
 */
class CategoryMapperTest {
    CategoryMapper mapper = CategoryMapper.INSTANCE;

    @Test
    void entityToDto() {
        Category source = createCategory(entityId, entityName, subEntityId, subEntityName);
        CategoryDto actual = mapper.entityToDto(source);
        CategoryDto expected = createCategoryDto(entityId, entityName);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }

    @Test
    void dtoToEntity() {
        CategoryDto source = createCategoryDto(entityId, entityName);
        Category actual = mapper.dtoToEntity(source);
        Category expected = ModelFactory.createCategory(entityId, entityName);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
