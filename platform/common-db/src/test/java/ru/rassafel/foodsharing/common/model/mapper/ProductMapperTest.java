package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.Product;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class ProductMapperTest {
    ProductMapper mapper = ProductMapper.INSTANCE;

    ProductDto sourceDto = new ProductDto();

    Product expectedEntity = new Product();

    Product sourceEntity = new Product();

    ProductDto expectedDto = new ProductDto();

    @BeforeEach
    void initValues() {
        sourceDto.setId(1L);
        sourceDto.setName("Test product");

        expectedEntity.setId(1L);
        expectedEntity.setName("Test product");
        expectedEntity.setCategory(null);

        sourceEntity.setId(1L);
        sourceEntity.setName("Test product");
        sourceEntity.setCategory(null);

        expectedDto.setId(1L);
        expectedDto.setName("Test product");
    }

    @Test
    void dtoToEntity() {
        Product actual = mapper.dtoToEntity(sourceDto);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(sourceDto)
            .isNotSameAs(expectedEntity)
            .isEqualToComparingFieldByField(expectedEntity);
    }

    @Test
    void entityToDto() {
        ProductDto actual = mapper.entityToDto(sourceEntity);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(sourceEntity)
            .isNotSameAs(expectedDto)
            .isEqualToComparingFieldByField(expectedDto);
    }
}
