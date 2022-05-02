package ru.rassafel.foodsharing.vkparser.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.common.exception.ApiException;
import ru.rassafel.foodsharing.vkparser.controller.GroupController;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.mapper.VkGroupMapper;
import ru.rassafel.foodsharing.vkparser.model.vk.group.FullAccessGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.WithoutAccessGroup;
import ru.rassafel.foodsharing.vkparser.service.GroupService;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class GroupControllerImpl implements GroupController {
    private final VkGroupMapper mapper;
    private final GroupService service;

    @Override
    public ResponseEntity<?> registerWithAccess(FullAccessGroup groupDto) {
        log.debug("Registration request with access received for group with id = {}", groupDto.getGroupId());
        VkGroup group = mapper.map(groupDto);
        try {
            service.registerWithAccess(group);
        } catch (ApiException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> registerWithoutAccess(WithoutAccessGroup groupDto) {
        log.debug("Registration request without access received for group with id = {}", groupDto.getGroupId());
        VkGroup group = mapper.map(groupDto);
        service.registerWithoutAccess(group);
        return ResponseEntity.ok().build();
    }
}
