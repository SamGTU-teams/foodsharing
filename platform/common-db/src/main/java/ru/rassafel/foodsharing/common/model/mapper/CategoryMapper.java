package ru.rassafel.foodsharing.common.model.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.model.dto.CategoryDto;
import ru.rassafel.foodsharing.common.model.entity.Category;

/**
 * @author rassafel
 */
@Component
public class CategoryMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Category.class, CategoryDto.class)
            .byDefault()
            .register();
    }
}
