package ru.rassafel.foodsharing.vkparser.service.impl;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.vkparser.config.ApplicationProperties;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.repository.GroupRepository;
import ru.rassafel.foodsharing.vkparser.service.GroupService;

import java.util.Objects;
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
    private final ApplicationProperties properties;

    @Override
    @SneakyThrows
    public VkGroup registerWithAccess(VkGroup group) {
        Integer groupId = group.getGroupId();

        GroupActor actor = new GroupActor(groupId, group.getAccessToken());

        group = findGroup(group);

        String confirmationCode = api.groups()
            .getCallbackConfirmationCode(actor, groupId)
            .execute()
            .getCode();
        group.setConfirmationCode(confirmationCode);
        repository.save(group);

        Integer serverId = api.groups()
            .addCallbackServer(actor, groupId, properties.getUrl(), properties.getServerTitle())
            .secretKey(group.getSecretKey())
            .execute()
            .getServerId();
        group.setServerId(serverId);
        repository.save(group);

        api.groups()
            .setCallbackSettings(actor, groupId)
            .apiVersion(api.getVersion())
            .serverId(serverId)
            .wallPostNew(true)
            .execute();

        return group;
    }

    @Override
    public VkGroup registerWithoutAccess(VkGroup group) {
        group = findGroup(group);
        return repository.save(group);
    }

    VkGroup findGroup(VkGroup group) {
        Optional<VkGroup> optionalGroup = repository.findById(group.getGroupId());
        if (optionalGroup.isPresent()) {
            VkGroup vkGroup = optionalGroup.get();
            throwIfSecretKeyNotMatch(group, vkGroup);
            vkGroup.setConfirmationCode(group.getConfirmationCode());
            vkGroup.setRegions(group.getRegions());
            vkGroup.setServerId(null);
            group = vkGroup;
        }
        return group;
    }

    void throwIfSecretKeyNotMatch(VkGroup accepted, VkGroup registered) {
        if (!Objects.equals(accepted.getSecretKey(), registered.getSecretKey())) {
            log.warn("Registration for group with id {} declined: Secret key do not match.", accepted.getGroupId());
//            ToDo: Create exception class.
            throw new RuntimeException("Secret key do not match.");
        }
    }
}
