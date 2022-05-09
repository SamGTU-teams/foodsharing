package ru.rassafel.foodsharing.vkbot.model.dto;

import com.vk.api.sdk.objects.callback.Type;
import lombok.Data;

@Data
public class VkUpdate {

    private Type type;

    private VkUpdateObject object;

}
