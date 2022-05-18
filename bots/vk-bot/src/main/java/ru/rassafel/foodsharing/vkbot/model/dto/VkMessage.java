package ru.rassafel.foodsharing.vkbot.model.dto;

import com.vk.api.sdk.objects.base.Geo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VkMessage {
    private int from_id;
    private String text;
    private Geo geo;
}
