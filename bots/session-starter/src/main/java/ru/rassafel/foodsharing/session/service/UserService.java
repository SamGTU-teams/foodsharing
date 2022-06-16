package ru.rassafel.foodsharing.session.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.session.model.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    void saveUser(User user);

    Optional<? extends User> getUser(Long id);

    List<UserPlacesAndProducts> findByFoodPost(FoodPostDto post);

    Optional<? extends User> getUserWithProducts(Long id);

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    class UserPlacesAndProducts {
        private Long userId = -1L;
        private Set<String> placeNames = new HashSet<>();
        private Set<String> productNames = new HashSet<>();
    }
}
