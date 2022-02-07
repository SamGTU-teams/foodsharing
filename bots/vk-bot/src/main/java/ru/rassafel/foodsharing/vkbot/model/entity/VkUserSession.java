package ru.rassafel.foodsharing.vkbot.model.entity;

import lombok.Data;

import javax.persistence.Embeddable;

/**
 * @author rassafel
 */
@Embeddable
@Data
public class VkUserSession {
    private Boolean active;

    private Integer step;

    private String name;
}
