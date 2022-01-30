package ru.rassafel.foodsharing.vkparser.model.vk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vk.api.sdk.events.Events;
import lombok.Data;

/**
 * @author rassafel
 */
@Data
public class CallbackMessage {
    @JsonProperty("type")
    private Events type;

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("object")
    private Wallpost wallpost;

    @JsonProperty("secret")
    private String secret;
}
