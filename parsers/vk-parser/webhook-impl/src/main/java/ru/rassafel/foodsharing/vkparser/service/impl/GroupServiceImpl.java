package ru.rassafel.foodsharing.vkparser.service.impl;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rassafel.foodsharing.vkparser.config.VkParserProperties;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.validator.SecretKeyValidator;
import ru.rassafel.foodsharing.vkparser.repository.GroupRepository;
import ru.rassafel.foodsharing.vkparser.service.GroupService;

import java.util.Optional;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {
    private final VkApiClient api;
    private final GroupRepository repository;
    private final VkParserProperties properties;
    private final SecretKeyValidator validator;

    @Override
    @Transactional
    public VkGroup registerWithAccess(VkGroup group) {
        Integer groupId = group.getGroupId();

        GroupActor actor = new GroupActor(groupId, group.getAccessToken());

        group = findGroup(group);

        String confirmationCode;
        try {
            log.debug("Try to get confirmation code for group with id = {}.", groupId);
            confirmationCode = api.groups()
                .getCallbackConfirmationCode(actor, groupId)
                .execute()
                .getCode();
        } catch (ClientException | ApiException ex) {
            log.warn("Fail query to confirmation code for group with id = {}.", groupId);
            throw new ru.rassafel.foodsharing.common.exception.ApiException(ex.getMessage());
        }
        group.setConfirmationCode(confirmationCode);

        Integer serverId;
        try {
            log.debug("Try to add callback server for group with id = {}.", groupId);
            serverId = api.groups()
                .addCallbackServer(actor, groupId, properties.getCallbackUrl(), properties.getServerTitle())
                .secretKey(group.getSecretKey())
                .execute()
                .getServerId();
        } catch (ClientException | ApiException ex) {
            log.warn("Fail query to add callback server for group with id = {}.", groupId);
            throw new ru.rassafel.foodsharing.common.exception.ApiException(ex.getMessage());
        }
        group.setServerId(serverId);

        try {
            log.debug("Try to edit configuration for group with id = {} and server id = {}.", groupId, serverId);
            api.groups()
                .setCallbackSettings(actor, groupId)
                .apiVersion(api.getVersion())
                .serverId(serverId)
                .wallPostNew(true)
                .execute();
        } catch (ClientException | ApiException ex) {
            log.warn("Fail query to edit configuration for group with id = {} and server id = {}.", groupId, serverId);
            throw new ru.rassafel.foodsharing.common.exception.ApiException(ex.getMessage());
        }

        repository.save(group);
        return group;
    }

    @Override
    public VkGroup registerWithoutAccess(VkGroup group) {
        group = findGroup(group);
        return repository.save(group);
    }

    private VkGroup findGroup(VkGroup group) {
        log.debug("Find group with id = {} in DB.", group.getGroupId());
        Optional<VkGroup> optionalGroup = repository.findById(group.getGroupId());
        if (optionalGroup.isPresent()) {
            log.debug("Group with id = {} exists in DB.", group.getGroupId());
            VkGroup vkGroup = optionalGroup.get();
            validator.validate(group, vkGroup.getSecretKey());
            vkGroup.setServerId(null);
            vkGroup.setAccessToken(group.getAccessToken());
            vkGroup.setConfirmationCode(group.getConfirmationCode());
            vkGroup.setRegions(group.getRegions());
            group = vkGroup;
        } else {
            log.debug("Group with id = {} does not exists in DB.", group.getGroupId());
        }
        return group;
    }
}
