package ru.rassafel.foodsharing.ad.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rassafel.foodsharing.ad.model.dto.AdPostDto;

/**
 * @author rassafel
 */
@RequestMapping(AdController.MAPPING)
public interface AdController {
    String MAPPING = "/ad";

    @PostMapping
    ResponseEntity<?> createAd(@RequestBody AdPostDto message);
}
