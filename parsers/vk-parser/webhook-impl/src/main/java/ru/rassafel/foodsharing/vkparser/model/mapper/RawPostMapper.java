package ru.rassafel.foodsharing.vkparser.model.mapper;

import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.parser.model.RawPost;
import ru.rassafel.foodsharing.vkparser.model.vk.Wallpost;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rassafel
 */
@Mapper(uses = {GeoMapper.class})
public abstract class RawPostMapper {
    public static final RawPostMapper INSTANCE = Mappers.getMapper(RawPostMapper.class);

    /**
     * Example: https://vk.com/wall-109125816_677938
     */
    public static final String WALLPOST_PATTERN = "https://vk.com/wall-%d_%d";

    /**
     * Example: https://vk.com/photo-109125816_457359241
     */
    public static final String PHOTO_PATTERN = "https://vk.com/photo-%d_%d";

    @AfterMapping
    protected void afterMapApiToDto(Wallpost source, @MappingTarget RawPost target) {
        int absOwnerId = Math.abs(source.getOwnerId());

        String wallpostUrl = String.format(WALLPOST_PATTERN, absOwnerId, source.getId());
        target.setUrl(wallpostUrl);

        List<String> attachments = Optional.ofNullable(source.getAttachments())
            .stream()
            .flatMap(Collection::stream)
            .filter(attachment -> WallpostAttachmentType.PHOTO.equals(attachment.getType()))
            .map(WallpostAttachment::getPhoto)
            .map(Photo::getId)
            .map(id -> String.format(PHOTO_PATTERN, absOwnerId, id))
            .collect(Collectors.toList());
        target.getContext().setAttachments(attachments);

        LocalDateTime date = LocalDateTime.ofEpochSecond(source.getDate().longValue(),
            0, ZoneOffset.UTC);
        target.setDate(date);
    }

    @Mappings({
        @Mapping(source = "geo", target = "context.point"),
        @Mapping(source = "text", target = "text"),
        @Mapping(source = "date", target = "date", ignore = true),
        @Mapping(target = "url", ignore = true),
        @Mapping(target = "context", ignore = true),
    })
    public abstract RawPost map(Wallpost source);
}
