package ru.rassafel.foodsharing.vkparser.model.vk.group.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.exception.ApiException;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.validator.SecretKeyValidator;

import java.util.Objects;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class SecretKeyValidatorImpl implements SecretKeyValidator {
    @Override
    public void validate(VkGroup group, String secretKey) {
        if (!Objects.equals(group.getSecretKey(), secretKey)) {
            Integer groupId = group.getGroupId();
            log.warn("Secret key do not match with registered key for group with id = {}", groupId);
//          ToDo: Create exception class.
            throw new ApiException("Secret key do not match.");
        }
    }
}
