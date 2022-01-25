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

    Category sourceEntity;

    Product sourceEntityProduct;

    CategoryDto expectedDto;

    CategoryDto sourceDto;

    Category expectedEntity;

    @BeforeEach
    void initValues() {
        sourceEntity = new Category();
        sourceEntity.setId(1L);
        sourceEntity.setName("Test category");

        sourceEntityProduct = new Product();
        sourceEntityProduct.setCategory(sourceEntity);
        sourceEntityProduct.setId(2L);
        sourceEntityProduct.setName("Test sourceEntityProduct");
        sourceEntity.setProducts(Set.of(sourceEntityProduct));

        expectedDto = new CategoryDto();
        expectedDto.setId(1L);
        expectedDto.setName("Test category");

        sourceDto = new CategoryDto();
        sourceDto.setId(1L);
        sourceDto.setName("Test category");

        expectedEntity = new Category();
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
