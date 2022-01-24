package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.Category;
import ru.rassafel.foodsharing.common.model.entity.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rassafel
 */
class ProductMapperTest {
    ProductMapper mapper = ProductMapper.INSTANCE;

    @Test
    void dtoToEntity() {
        ProductDto source = new ProductDto();
        source.setId(1L);
        source.setName("Test product");

        Product expected = new Product();
        expected.setId(1L);
        expected.setName("Test product");
        expected.setCategory(null);

        Product actual = mapper.dtoToEntity(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }

    @Test
    void entityToDto() {
        Product source = new Product();
        source.setId(1L);
        source.setName("Test product");
        source.setCategory(null);

        ProductDto expected = new ProductDto();
        expected.setId(1L);
        expected.setName("Test product");

        ProductDto actual = mapper.entityToDto(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
