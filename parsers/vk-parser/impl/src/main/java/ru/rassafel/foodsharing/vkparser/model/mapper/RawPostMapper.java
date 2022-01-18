package ru.rassafel.foodsharing.vkparser.model.mapper;

import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.Geo;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.vkparser.model.dto.RawPostDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * @author rassafel
 */
@Component
@Slf4j
public class RawPostMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(RawPostDto.class, Wallpost.class)
            .customize(new CustomMapper<RawPostDto, Wallpost>() {
                @Override
                public void mapBtoA(Wallpost source, RawPostDto dest, MappingContext context) {
//                    https://vk.com/club109125816?w=wall-109125816_677938
//                    https://vk.com/club109125816?z=photo-109125816_457359241%2Fwall-109125816_677938
                    int absOwnerId = Math.abs(source.getOwnerId());

                    String groupUrl = String.format("https://vk.com/club%d", absOwnerId);
                    String wallpostParam = String.format("wall-%d_%d", absOwnerId, source.getId());

                    List<String> attachments = source.getAttachments()
                        .stream()
                        .filter(attachment -> WallpostAttachmentType.PHOTO.equals(attachment.getType()))
                        .map(attachment -> {
                            Photo photo = attachment.getPhoto();
                            return String.format("%s?z=photo-%d_%d%%2F%s",
                                groupUrl, absOwnerId, photo.getId(), wallpostParam);
                        })
                        .collect(Collectors.toList());

                    dest.setAttachments(attachments);
                    dest.setUrl(String.format("%s?w=%s", groupUrl, wallpostParam));


                    dest.setDate(LocalDateTime.ofEpochSecond(source.getDate().longValue(),
                        0, ZoneOffset.UTC));

                    Geo geo = source.getGeo();
                    if (nonNull(geo)) {
                        try {
                            double[] coordinates = Arrays.stream(geo.getCoordinates().split("\\s+"))
                                .mapToDouble(Double::parseDouble)
                                .toArray();

                            GeoPoint point = new GeoPoint();
                            point.setLat(coordinates[0]);
                            point.setLat(coordinates[1]);
                            dest.setPoint(point);
                        } catch (RuntimeException ex) {
                            log.warn("Exception on parse coordinates", ex);
                        }
                    }
                }
            })
            .field("text", "text")
            .byDefault()
            .register();
    }
}
