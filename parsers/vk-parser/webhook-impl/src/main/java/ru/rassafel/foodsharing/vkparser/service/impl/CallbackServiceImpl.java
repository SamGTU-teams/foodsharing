package ru.rassafel.foodsharing.vkparser.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.common.exception.ApiException;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;
import ru.rassafel.foodsharing.common.model.mapper.RegionMapper;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.mapper.RawPostMapper;
import ru.rassafel.foodsharing.vkparser.model.vk.Wallpost;
import ru.rassafel.foodsharing.vkparser.model.vk.group.validator.SecretKeyValidator;
import ru.rassafel.foodsharing.vkparser.repository.GroupRepository;
import ru.rassafel.foodsharing.vkparser.service.CallbackService;

import java.util.List;
import java.util.Optional;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class CallbackServiceImpl implements CallbackService {
    private final GroupRepository repository;
    private final RabbitTemplate rawRabbitTemplate;
    private final RawPostMapper rawPostMapper;
    private final RegionMapper regionMapper;
    private final SecretKeyValidator validator;

    @Override
    public RawPostDto wallpostNew(Integer groupId, Wallpost wallpost, String secret) {
        VkGroup vkGroup = findGroup(groupId, secret);

        RawPostDto postDto = rawPostMapper.map(wallpost);
        List<RegionDto> regions = regionMapper.entitiesToDtos(vkGroup.getRegions());
        postDto.getContext().setRegions(regions);

        rawRabbitTemplate.convertAndSend(postDto);
        return postDto;
    }

    @Override
    public String confirmation(Integer groupId) {
        return findGroup(groupId)
            .getConfirmationCode();
    }

    private VkGroup findGroup(Integer groupId) {
        log.debug("Find group with id = {} in DB.", groupId);
        VkGroup group;
        Optional<VkGroup> optionalGroup = repository.findById(groupId);
        if (optionalGroup.isPresent()) {
            log.debug("Group with id = {} exists in DB.", groupId);
            group = optionalGroup.get();
        } else {
            log.warn("Group with id = {} does not exists in DB.", groupId);
//          ToDo: Create exception class.
            throw new ApiException("Group not registered.");
        }
        return group;
    }

    private VkGroup findGroup(Integer groupId, String secret) {
        VkGroup group = findGroup(groupId);
        validator.validate(group, secret);
        return group;
    }
}
