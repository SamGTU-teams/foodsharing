package ru.rassafel.foodsharing.vkparser.model.vk.group;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author rassafel
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WithoutAccessGroup extends GroupParams {
    @NotNull
    private String confirmationCode;
}
