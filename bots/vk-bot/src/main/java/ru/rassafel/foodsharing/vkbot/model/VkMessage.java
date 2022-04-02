package ru.rassafel.foodsharing.vkbot.model;

import com.vk.api.sdk.objects.base.Geo;
import lombok.Data;

@Data
public class VkMessage {
    public int date;
    public int from_id;
    public int id;
    public int out;
    public int peer_id;
    public String text;
    public int conversation_message_id;
    //    public ArrayList<Object> fwd_messages;
    public boolean important;
    public int random_id;
    //    public ArrayList<Object> attachments;
    public boolean is_hidden;
    private Geo geo;
}
