package ru.rassafel.foodsharing.common.model.mapper;

import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;
import ru.rassafel.foodsharing.common.model.entity.geo.Region;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.rassafel.foodsharing.common.model.ModelFactory.*;

/**
 * @author rassafel
 */
class RegionMapperTest {
    final RegionMapper mapper = RegionMapper.INSTANCE;

    @Test
    void dtoToEntity() {
        RegionDto source = createRegionDto(entityId, entityName, 1f, 1f);
        Region actual = mapper.dtoToEntity(source);
        Region expected = createRegion(entityId, entityName, 1f, 1f);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected)
            .extracting(Region::getPoint)
            .isNotNull()
            .isNotSameAs(source.getPoint())
            .isNotSameAs(expected.getPoint())
            .isEqualToComparingFieldByField(expected.getPoint());
    }

    @Test
    void entityToDto() {
        Region source = createRegion(entityId, entityName, 1f, 1f);
        RegionDto actual = mapper.entityToDto(source);
        RegionDto expected = createRegionDto(entityId, entityName, 1f, 1f);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected)
            .extracting(RegionDto::getPoint)
            .isNotNull()
            .isNotSameAs(source.getPoint())
            .isNotSameAs(expected.getPoint())
            .isEqualToComparingFieldByField(expected.getPoint());
    }

    @Test
    void setEntitiesToDtos() {
        Set<Region> source = Set.of(createRegion(entityId, entityName, 1f, 1f));

        Set<RegionDto> expected = Set.of(createRegionDto(entityId, entityName, 1f, 1f));

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
        List<Region> source = List.of(createRegion(entityId, entityName, 1f, 1f));

        List<RegionDto> expected = List.of(createRegionDto(entityId, entityName, 1f, 1f));

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
        Set<RegionDto> source = Set.of(createRegionDto(entityId, entityName, 1f, 1f));

        Set<Region> expected = Set.of(createRegion(entityId, entityName, 1f, 1f));

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
        List<RegionDto> source = List.of(createRegionDto(entityId, entityName, 1f, 1f));

        List<Region> expected = List.of(createRegion(entityId, entityName, 1f, 1f));

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
