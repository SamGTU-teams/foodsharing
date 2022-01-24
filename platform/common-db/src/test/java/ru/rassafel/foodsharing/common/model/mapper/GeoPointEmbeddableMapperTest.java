package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.GeoPointEmbeddable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rassafel
 */
class GeoPointEmbeddableMapperTest {
    GeoPointEmbeddableMapper mapper = GeoPointEmbeddableMapper.INSTANCE;

    @Test
    void entityToDto() {
        GeoPointEmbeddable source = new GeoPointEmbeddable();
        source.setLat(1f);
        source.setLon(1f);

        GeoPoint expected = new GeoPoint();
        expected.setLat(1f);
        expected.setLon(1f);

        GeoPoint actual = mapper.entityToDto(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }

    @Test
    void dtoToEntity() {
        GeoPoint source = new GeoPoint();
        source.setLat(1f);
        source.setLon(1f);

        GeoPointEmbeddable expected = new GeoPointEmbeddable();
        expected.setLat(1f);
        expected.setLon(1f);

        GeoPointEmbeddable actual = mapper.dtoToEntity(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
