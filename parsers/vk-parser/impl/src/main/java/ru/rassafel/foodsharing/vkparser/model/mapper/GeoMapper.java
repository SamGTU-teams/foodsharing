package ru.rassafel.foodsharing.vkparser.model.mapper;

import com.vk.api.sdk.objects.wall.Geo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.common.model.GeoPoint;

import java.util.Arrays;

/**
 * @author rassafel
 */
@Mapper
public abstract class GeoMapper {
    public static final GeoMapper INSTANCE = Mappers.getMapper(GeoMapper.class);

    public GeoPoint map(Geo source) {
        GeoPoint point = new GeoPoint();
        double[] coordinates = Arrays.stream(source.getCoordinates().split("\\s+"))
            .mapToDouble(Double::parseDouble)
            .toArray();

        point.setLat(coordinates[0]);
        point.setLon(coordinates[1]);
        return point;
    }
}