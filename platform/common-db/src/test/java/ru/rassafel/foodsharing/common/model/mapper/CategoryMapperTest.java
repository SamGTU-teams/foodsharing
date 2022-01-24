package ru.rassafel.foodsharing.common.model.mapper;

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

    @Test
    void entityToDto() {
        Category source = new Category();
        source.setId(1L);
        source.setName("Test category");

        Product product = new Product();
        product.setCategory(source);
        product.setId(2L);
        product.setName("Test product");
        source.setProducts(Set.of(product));

        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Test category");

        CategoryDto actual = mapper.entityToDto(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }

    @Test
    void dtoToEntity() {
        CategoryDto source = new CategoryDto();
        source.setId(1L);
        source.setName("Test category");

        Category expected = new Category();
        expected.setId(1L);
        expected.setName("Test category");
        expected.setProducts(null);

        Category actual = mapper.dtoToEntity(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
