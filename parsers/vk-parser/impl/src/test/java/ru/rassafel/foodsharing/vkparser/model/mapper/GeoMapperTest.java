package ru.rassafel.foodsharing.vkparser.model.mapper;

import com.vk.api.sdk.objects.wall.Geo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rassafel.foodsharing.common.model.GeoPoint;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
@SpringBootTest(classes = GeoMapperImpl.class)
class GeoMapperTest {
    @Autowired
    GeoMapper mapper;

    @Test
    void apiToDto() {
        Geo source = new Geo()
            .setCoordinates("51.123 15.321");

        GeoPoint expected = new GeoPoint();
        expected.setLat(51.123d);
        expected.setLon(15.321d);

        GeoPoint actual = mapper.apiToDto(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
