package ru.rassafel.foodsharing.vkbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vk.api.sdk.objects.callback.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VkUpdate {
    @JsonProperty("type")
    private Type type;
    @JsonProperty("object")
    private VkUpdateObject object;
}
