package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.Product;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
@SpringBootTest(classes = ProductMapperImpl.class)
class ProductMapperTest {
    @Autowired
    ProductMapper mapper;

    ProductDto sourceDto;

    Product expectedEntity;

    Product sourceEntity;

    ProductDto expectedDto;

    @BeforeEach
    void initValues() {
        sourceDto = new ProductDto();
        sourceDto.setId(1L);
        sourceDto.setName("Test product");

        expectedEntity = new Product();
        expectedEntity.setId(1L);
        expectedEntity.setName("Test product");
        expectedEntity.setCategory(null);

        sourceEntity = new Product();
        sourceEntity.setId(1L);
        sourceEntity.setName("Test product");
        sourceEntity.setCategory(null);

        expectedDto = new ProductDto();
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
