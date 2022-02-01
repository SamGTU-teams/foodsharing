package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.GeoPointEmbeddable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class GeoPointEmbeddableMapperTest {
    GeoPointEmbeddableMapper mapper = GeoPointEmbeddableMapper.INSTANCE;

    GeoPointEmbeddable sourceEntity = new GeoPointEmbeddable();

    GeoPoint expectedDto = new GeoPoint();

    GeoPoint sourceDto = new GeoPoint();

    GeoPointEmbeddable expectedEntity = new GeoPointEmbeddable();

    @BeforeEach
    void initValues() {
        sourceEntity.setLat(1f);
        sourceEntity.setLon(1f);

        expectedDto.setLat(1f);
        expectedDto.setLon(1f);

        sourceDto.setLat(1f);
        sourceDto.setLon(1f);

        expectedEntity.setLat(1f);
        expectedEntity.setLon(1f);
    }

    @Test
    void entityToDto() {

        GeoPoint actual = mapper.entityToDto(sourceEntity);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(sourceEntity)
            .isNotSameAs(expectedDto)
            .isEqualToComparingFieldByField(expectedDto);
    }

    @Test
    void dtoToEntity() {
        GeoPointEmbeddable actual = mapper.dtoToEntity(sourceDto);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(sourceDto)
            .isNotSameAs(expectedEntity)
            .isEqualToComparingFieldByField(expectedEntity);
    }
}
