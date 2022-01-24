package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.GeoPointEmbeddable;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;
import ru.rassafel.foodsharing.common.model.entity.Region;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rassafel
 */
class RegionMapperTest {
    RegionMapper mapper = RegionMapper.INSTANCE;

    @Test
    void dtoToEntity() {
        RegionDto source = new RegionDto();
        source.setId(1L);
        source.setName("Test region");

        GeoPoint sourcePoint = new GeoPoint();
        sourcePoint.setLat(1f);
        sourcePoint.setLon(1f);
        source.setPoint(sourcePoint);

        Region expected = new Region();
        expected.setId(1L);
        expected.setName("Test region");
        GeoPointEmbeddable expectedPoint = new GeoPointEmbeddable();
        expectedPoint.setLat(1f);
        expectedPoint.setLon(1f);
        expected.setPoint(expectedPoint);

        Region actual = mapper.dtoToEntity(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);

        assertThat(actual.getPoint())
            .isNotNull()
            .isNotSameAs(sourcePoint)
            .isNotSameAs(expectedPoint)
            .isEqualToComparingFieldByField(expectedPoint);
    }

    @Test
    void entityToDto() {
        Region source = new Region();
        source.setId(1L);
        source.setName("Test region");
        GeoPointEmbeddable sourcePoint = new GeoPointEmbeddable();
        sourcePoint.setLat(1f);
        sourcePoint.setLon(1f);
        source.setPoint(sourcePoint);

        RegionDto expected = new RegionDto();
        expected.setId(1L);
        expected.setName("Test region");

        GeoPoint expectedPoint = new GeoPoint();
        expectedPoint.setLat(1f);
        expectedPoint.setLon(1f);
        expected.setPoint(expectedPoint);

        RegionDto actual = mapper.entityToDto(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);

        assertThat(actual.getPoint())
            .isNotNull()
            .isNotSameAs(sourcePoint)
            .isNotSameAs(expectedPoint)
            .isEqualToComparingFieldByField(expectedPoint);
    }
}
