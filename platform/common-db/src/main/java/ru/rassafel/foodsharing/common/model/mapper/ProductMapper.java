package ru.rassafel.foodsharing.common.model.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.Product;

/**
 * @author rassafel
 */
@Component
public class ProductMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Product.class, ProductDto.class)
            .byDefault()
            .register();
    }
}
