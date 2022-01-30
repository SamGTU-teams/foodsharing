package ru.rassafel.foodsharing.vkparser.model.vk.deserializer;

import com.vk.api.sdk.objects.photos.PhotoSizesType;
import org.springframework.boot.jackson.JsonComponent;

/**
 * @author rassafel
 */
@JsonComponent
public class PhotoSizesTypeDeserializer extends EnumDeserializer<PhotoSizesType> {
    public PhotoSizesTypeDeserializer() {
        super(PhotoSizesType.class);
    }
}
