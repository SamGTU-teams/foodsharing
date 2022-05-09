package ru.rassafel.foodsharing.vkbot.model.dto;

import lombok.Data;

@Data
public class VkUpdateObject {
    public VkMessage message;
    public ClientInfo client_info;
}
