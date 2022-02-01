package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.dto.CategoryDto;
import ru.rassafel.foodsharing.common.model.entity.Category;
import ru.rassafel.foodsharing.common.model.entity.Product;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class CategoryMapperTest {
    CategoryMapper mapper = CategoryMapper.INSTANCE;

    Category sourceEntity = new Category();

    Product sourceEntityProduct = new Product();

    CategoryDto expectedDto = new CategoryDto();

    CategoryDto sourceDto = new CategoryDto();

    Category expectedEntity = new Category();

    @BeforeEach
    void initValues() {
        sourceEntity.setId(1L);
        sourceEntity.setName("Test category");
        sourceEntity.setProducts(Set.of(sourceEntityProduct));

        sourceEntityProduct.setCategory(sourceEntity);
        sourceEntityProduct.setId(2L);
        sourceEntityProduct.setName("Test sourceEntityProduct");

        expectedDto.setId(1L);
        expectedDto.setName("Test category");

        sourceDto.setId(1L);
        sourceDto.setName("Test category");

        expectedEntity.setId(1L);
        expectedEntity.setName("Test category");
        expectedEntity.setProducts(null);
    }

    @Test
    void entityToDto() {
        CategoryDto actual = mapper.entityToDto(sourceEntity);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(sourceEntity)
            .isNotSameAs(expectedDto)
            .isEqualToComparingFieldByField(expectedDto);
    }

    @Test
    void dtoToEntity() {
        Category actual = mapper.dtoToEntity(sourceDto);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(sourceDto)
            .isNotSameAs(expectedEntity)
            .isEqualToComparingFieldByField(expectedEntity);
    }
}
