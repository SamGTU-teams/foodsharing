package ru.rassafel.foodsharing.vkparser.model.vk.group;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author rassafel
 */
@Data
public class FullAccessGroup extends GroupParams {
    @NotNull
    private String accessToken;
}
