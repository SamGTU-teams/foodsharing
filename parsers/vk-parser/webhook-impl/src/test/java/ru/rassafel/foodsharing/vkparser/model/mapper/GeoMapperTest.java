package ru.rassafel.foodsharing.vkparser.model.mapper;

import com.vk.api.sdk.objects.wall.Geo;
import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.GeoPoint;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class GeoMapperTest {
    final GeoMapper mapper = GeoMapper.INSTANCE;

    @Test
    void apiToDto() {
        Geo source = new Geo()
            .setCoordinates("51.123 15.321");

        GeoPoint expected = new GeoPoint();
        expected.setLat(51.123d);
        expected.setLon(15.321d);

        GeoPoint actual = mapper.map(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
