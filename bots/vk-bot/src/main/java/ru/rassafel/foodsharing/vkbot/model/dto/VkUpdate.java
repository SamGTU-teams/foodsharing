package ru.rassafel.foodsharing.vkbot.model.dto;

import com.vk.api.sdk.objects.callback.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VkUpdate {

    private Type type;

    private VkUpdateObject object;

}
