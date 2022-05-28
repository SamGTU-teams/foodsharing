package ru.rassafel.foodsharing.vkbot.model.vk;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private int id;
    @JsonProperty("from_id")
    private int fromId;
    @JsonProperty("text")
    private String text;
    @JsonProperty("geo")
    private Geo geo;
}
