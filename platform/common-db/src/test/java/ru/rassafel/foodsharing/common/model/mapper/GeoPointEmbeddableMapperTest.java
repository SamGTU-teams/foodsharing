package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.entity.geo.GeoPointEmbeddable;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.common.model.ModelFactory.createPoint;
import static ru.rassafel.foodsharing.common.model.ModelFactory.createPointDto;

/**
 * @author rassafel
 */
class GeoPointEmbeddableMapperTest {
    final GeoPointEmbeddableMapper mapper = GeoPointEmbeddableMapper.INSTANCE;

    @Test
    void entityToDto() {
        GeoPointEmbeddable source = createPoint(1f, 1f);
        GeoPoint actual = mapper.entityToDto(source);
        GeoPoint expected = createPointDto(1f, 1f);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }

    @Test
    void dtoToEntity() {
        GeoPoint source = createPointDto(1f, 1f);
        GeoPointEmbeddable actual = mapper.dtoToEntity(source);
        GeoPointEmbeddable expected = createPoint(1f, 1f);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
