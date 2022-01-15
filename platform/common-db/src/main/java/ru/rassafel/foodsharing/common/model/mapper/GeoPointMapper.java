package ru.rassafel.foodsharing.common.model.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.GeoPointEmbeddable;

/**
 * @author rassafel
 */
@Component
public class GeoPointMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(GeoPointEmbeddable.class, GeoPoint.class)
            .byDefault()
            .register();
    }
}
