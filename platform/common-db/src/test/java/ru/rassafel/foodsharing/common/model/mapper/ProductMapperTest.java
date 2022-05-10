package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.common.model.ModelFactory.*;

/**
 * @author rassafel
 */
class ProductMapperTest {
    final ProductMapper mapper = ProductMapper.INSTANCE;

    @Test
    void dtoToEntity() {
        ProductDto source = createProductDto(entityId, entityName);
        Product actual = mapper.dtoToEntity(source);
        Product expected = createProduct(entityId, entityName);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }

    @Test
    void entityToDto() {
        Product source = createProduct(entityId, entityName);
        ProductDto actual = mapper.entityToDto(source);
        ProductDto expected = createProductDto(entityId, entityName);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
