package ru.rassafel.foodsharing.tgbot.model.entity;

import lombok.Data;

import javax.persistence.Embeddable;

/**
 * @author rassafel
 */
@Embeddable
@Data
public class TgUserSession {
    private Boolean active;

    private Integer step;

    private String name;
}
