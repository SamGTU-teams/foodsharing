package ru.rassafel.foodsharing.vkparser.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rassafel.foodsharing.vkparser.model.vk.group.FullAccessGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.WithoutAccessGroup;

import javax.validation.Valid;

/**
 * @author rassafel
 */
@RequestMapping(GroupController.MAPPING)
public interface GroupController {
    String MAPPING = "/register";

    @PostMapping
    ResponseEntity<?> registerWithAccess(@Valid @RequestBody FullAccessGroup request);

    @PutMapping
    ResponseEntity<?> registerWithoutAccess(@Valid @RequestBody WithoutAccessGroup request);
}
