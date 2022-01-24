package ru.rassafel.foodsharing.vkparser.model.vk.group;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author rassafel
 */
@Data
public class GroupParams {
    @NotNull
    private Integer groupId;

    private String secretKey;
}
