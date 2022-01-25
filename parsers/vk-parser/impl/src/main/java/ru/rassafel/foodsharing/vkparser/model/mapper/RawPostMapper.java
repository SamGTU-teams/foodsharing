package ru.rassafel.foodsharing.vkparser.model.mapper;

import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;
import ru.rassafel.foodsharing.vkparser.model.vk.Wallpost;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rassafel
 */
@Mapper(componentModel = "spring", uses = {GeoMapper.class})
public abstract class RawPostMapper {
    public static final RawPostMapper INSTANCE = Mappers.getMapper(RawPostMapper.class);

    @AfterMapping
    protected void afterMapApiToDto(Wallpost source, @MappingTarget RawPostDto target) {
//        https://vk.com/club109125816?w=wall-109125816_677938
//        https://vk.com/club109125816?z=photo-109125816_457359241%2Fwall-109125816_677938
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

        target.getContext().setAttachments(attachments);

        target.setUrl(String.format("%s?w=%s", groupUrl, wallpostParam));

        target.setDate(LocalDateTime.ofEpochSecond(source.getDate().longValue(),
            0, ZoneOffset.UTC));
    }

    @Mappings({
        @Mapping(source = "geo", target = "context.point"),
        @Mapping(source = "text", target = "text"),
        @Mapping(source = "date", target = "date", ignore = true)
    })
    public abstract RawPostDto map(Wallpost source);
}
