package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(classes = {RegionMapperImpl.class, GeoPointEmbeddableMapperImpl.class})
class RegionMapperTest {
    @Autowired
    RegionMapper mapper;

    RegionDto sourceDto;

    GeoPoint sourcePointDto;

    Region expectedEntity;

    GeoPointEmbeddable expectedEntityPoint;

    Region sourceEntity;

    GeoPointEmbeddable sourceEntityPoint;

    RegionDto expectedDto;

    GeoPoint expectedDtoPoint;

    @BeforeEach
    void initValues() {
        sourceDto = new RegionDto();
        sourceDto.setId(1L);
        sourceDto.setName("Test region");

        sourcePointDto = new GeoPoint();
        sourcePointDto.setLat(1f);
        sourcePointDto.setLon(1f);
        sourceDto.setPoint(sourcePointDto);

        expectedEntity = new Region();
        expectedEntity.setId(1L);
        expectedEntity.setName("Test region");
        expectedEntityPoint = new GeoPointEmbeddable();
        expectedEntityPoint.setLat(1f);
        expectedEntityPoint.setLon(1f);
        expectedEntity.setPoint(expectedEntityPoint);

        sourceEntity = new Region();
        sourceEntity.setId(1L);
        sourceEntity.setName("Test region");
        sourceEntityPoint = new GeoPointEmbeddable();
        sourceEntityPoint.setLat(1f);
        sourceEntityPoint.setLon(1f);
        sourceEntity.setPoint(sourceEntityPoint);

        expectedDto = new RegionDto();
        expectedDto.setId(1L);
        expectedDto.setName("Test region");

        expectedDtoPoint = new GeoPoint();
        expectedDtoPoint.setLat(1f);
        expectedDtoPoint.setLon(1f);
        expectedDto.setPoint(expectedDtoPoint);
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
