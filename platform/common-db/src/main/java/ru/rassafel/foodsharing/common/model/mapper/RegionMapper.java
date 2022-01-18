package ru.rassafel.foodsharing.common.model.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;
import ru.rassafel.foodsharing.common.model.entity.Region;

/**
 * @author rassafel
 */
@Component
public class RegionMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Region.class, RegionDto.class)
            .byDefault()
            .register();
    }
}
