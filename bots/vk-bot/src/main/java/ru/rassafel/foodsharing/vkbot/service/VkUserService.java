package ru.rassafel.foodsharing.vkbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VkUserService implements UserService {
    private final VkUserRepository repository;

    @Override
    public void saveUser(User user) {
        if (!(user instanceof VkUser)) {
            throw new IllegalArgumentException("User is not from VK!");
        }
        repository.save((VkUser) user);
    }

    @Override
    public Optional<? extends User> getUser(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Object[]> findByFoodPost(FoodPostDto post) {
        List<Long> productIds = post.getProducts().stream().map(ProductDto::getId).collect(Collectors.toList());
        GeoPoint point = post.getPoint();
        return repository.findByProductAndSuitablePlace(productIds, point.getLat(), point.getLon());
    }

    @Override
    public Optional<? extends User> getUserWithProducts(Long id) {
        return repository.findWithProductsById(id);
    }
}
