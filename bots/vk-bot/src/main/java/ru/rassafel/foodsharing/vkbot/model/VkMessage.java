package ru.rassafel.foodsharing.vkbot.model;

import com.vk.api.sdk.objects.base.Geo;
import lombok.Data;

@Data
public class VkMessage {
    private int date;
    private int from_id;
    private int id;
    private int out;
    private int peer_id;
    private String text;
    private int conversation_message_id;
    private boolean important;
    private int random_id;
    private boolean is_hidden;
    private Geo geo;
}
