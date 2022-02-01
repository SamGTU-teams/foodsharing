package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.GeoPointEmbeddable;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;
import ru.rassafel.foodsharing.common.model.entity.Region;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class RegionMapperTest {
    RegionMapper mapper = RegionMapper.INSTANCE;

    RegionDto sourceDto = new RegionDto();

    GeoPoint sourcePointDto = new GeoPoint();

    Region expectedEntity = new Region();

    GeoPointEmbeddable expectedEntityPoint = new GeoPointEmbeddable();

    Region sourceEntity = new Region();

    GeoPointEmbeddable sourceEntityPoint = new GeoPointEmbeddable();

    RegionDto expectedDto = new RegionDto();

    GeoPoint expectedDtoPoint = new GeoPoint();

    @BeforeEach
    void initValues() {
        sourceDto.setId(1L);
        sourceDto.setName("Test region");
        sourceDto.setPoint(sourcePointDto);

        sourcePointDto.setLat(1f);
        sourcePointDto.setLon(1f);

        expectedEntity.setId(1L);
        expectedEntity.setName("Test region");
        expectedEntity.setPoint(expectedEntityPoint);

        expectedEntityPoint.setLat(1f);
        expectedEntityPoint.setLon(1f);

        sourceEntity.setId(1L);
        sourceEntity.setName("Test region");
        sourceEntity.setPoint(sourceEntityPoint);

        sourceEntityPoint.setLat(1f);
        sourceEntityPoint.setLon(1f);

        expectedDto.setId(1L);
        expectedDto.setName("Test region");
        expectedDto.setPoint(expectedDtoPoint);

        expectedDtoPoint.setLat(1f);
        expectedDtoPoint.setLon(1f);
    }

    @Test
    void dtoToEntity() {
        Region actual = mapper.dtoToEntity(sourceDto);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(sourceDto)
            .isNotSameAs(expectedEntity)
            .isEqualToComparingFieldByField(expectedEntity);

        assertThat(actual.getPoint())
            .isNotNull()
            .isNotSameAs(sourcePointDto)
            .isNotSameAs(expectedEntityPoint)
            .isEqualToComparingFieldByField(expectedEntityPoint);
    }

    @Test
    void entityToDto() {
        RegionDto actual = mapper.entityToDto(sourceEntity);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(sourceEntity)
            .isNotSameAs(expectedDto)
            .isEqualToComparingFieldByField(expectedDto);

        assertThat(actual.getPoint())
            .isNotNull()
            .isNotSameAs(sourceEntityPoint)
            .isNotSameAs(expectedDtoPoint)
            .isEqualToComparingFieldByField(expectedDtoPoint);
    }

    @Test
    void setEntitiesToDtos() {
        Set<Region> source = Set.of(sourceEntity);

        Set<RegionDto> expected = Set.of(expectedDto);

        Set<RegionDto> actual = mapper.entitiesToDtos(source);

        assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .containsAll(expected)
            .isEqualTo(expected);
    }

    @Test
    void listEntitiesToDtos() {
        List<Region> source = List.of(sourceEntity);

        List<RegionDto> expected = List.of(expectedDto);

        List<RegionDto> actual = mapper.entitiesToDtos(source);

        assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .containsAll(expected)
            .isEqualTo(expected);
    }

    @Test
    void setDtosToEntities() {
        Set<RegionDto> source = Set.of(sourceDto);

        Set<Region> expected = Set.of(expectedEntity);

        Set<Region> actual = mapper.dtosToEntities(source);

        assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .containsAll(expected)
            .isEqualTo(expected);
    }

    @Test
    void listDtosToEntities() {
        List<RegionDto> source = List.of(sourceDto);

        List<Region> expected = List.of(expectedEntity);

        List<Region> actual = mapper.dtosToEntities(source);

        assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .containsAll(expected)
            .isEqualTo(expected);
    }
}
