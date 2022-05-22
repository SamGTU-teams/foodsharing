package ru.rassafel.bot.session.service;

import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(User user);

    Optional<? extends User> getUser(Long id);

    List<Object[]> findByFoodPost(FoodPostDto post);

    Optional<? extends User> getUserWithProducts(Long id);
}
