package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.GeoPointEmbeddable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
@SpringBootTest(classes = GeoPointEmbeddableMapperImpl.class)
class GeoPointEmbeddableMapperTest {
    @Autowired
    GeoPointEmbeddableMapper mapper;

    GeoPointEmbeddable sourceEntity;

    GeoPoint expectedDto;

    GeoPoint sourceDto;

    GeoPointEmbeddable expectedEntity;

    @BeforeEach
    void initValues() {
        sourceEntity = new GeoPointEmbeddable();
        sourceEntity.setLat(1f);
        sourceEntity.setLon(1f);

        expectedDto = new GeoPoint();
        expectedDto.setLat(1f);
        expectedDto.setLon(1f);

        sourceDto = new GeoPoint();
        sourceDto.setLat(1f);
        sourceDto.setLon(1f);

        expectedEntity = new GeoPointEmbeddable();
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
