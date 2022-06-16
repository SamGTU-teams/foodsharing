package ru.rassafel.foodsharing.vkbot.model.vk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VkUpdateObject {
    @JsonProperty("message")
    public VkMessage message;
}
