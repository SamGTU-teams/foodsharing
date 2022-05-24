package ru.rassafel.foodsharing.vkparser.model.mapper;

import com.vk.api.sdk.objects.wall.Geo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.GeoPoint;

import java.util.regex.Pattern;

import static java.util.Objects.isNull;

/**
 * @author rassafel
 */
@Mapper
public abstract class GeoMapper {
    public static final GeoMapper INSTANCE = Mappers.getMapper(GeoMapper.class);

    public GeoPoint map(Geo source) {
        if (isNull(source)) {
            return null;
        }

        double[] coordinates = Pattern.compile("\\s+")
            .splitAsStream(source.getCoordinates().strip())
            .mapToDouble(Double::parseDouble)
            .toArray();
        return new GeoPoint(coordinates[0], coordinates[1]);
    }
}
