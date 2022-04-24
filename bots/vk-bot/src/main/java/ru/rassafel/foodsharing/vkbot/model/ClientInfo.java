package ru.rassafel.foodsharing.vkbot.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ClientInfo {
    public ArrayList<String> button_actions;
    public boolean keyboard;
    public boolean inline_keyboard;
    public boolean carousel;
    public int lang_id;
}
