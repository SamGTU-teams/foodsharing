package ru.rassafel.foodsharing.vkparser.model.vk.deserializer;

import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import org.springframework.boot.jackson.JsonComponent;

/**
 * @author rassafel
 */
@JsonComponent
public class WallpostAttachmentTypeDeserializer extends EnumDeserializer<WallpostAttachmentType> {
    public WallpostAttachmentTypeDeserializer() {
        super(WallpostAttachmentType.class);
    }
}
