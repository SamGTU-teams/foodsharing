package ru.rassafel.foodsharing.session.service;

import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;

/**
 * @author rassafel
 */
public interface FoodPostHandler {
    void handle(FoodPostDto foodPostDto);
}
