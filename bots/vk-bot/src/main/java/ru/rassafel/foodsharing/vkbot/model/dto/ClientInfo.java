package ru.rassafel.foodsharing.vkbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ClientInfo {
    @JsonProperty("button_actions")
    public ArrayList<String> buttonActions;
    @JsonProperty("keyboard")
    public boolean keyboard;
    @JsonProperty("inline_keyboard")
    public boolean inlineKeyboard;
    @JsonProperty("carousel")
    public boolean carousel;
    @JsonProperty("lang_id")
    public int langId;
}
