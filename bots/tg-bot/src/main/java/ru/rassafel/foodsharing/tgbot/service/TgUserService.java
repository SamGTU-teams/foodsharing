package ru.rassafel.foodsharing.tgbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.tgbot.model.TgUser;
import ru.rassafel.foodsharing.tgbot.repository.TgUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TgUserService implements UserService {
    private final TgUserRepository repository;

    @Override
    public void saveUser(User user) {
        if (!(user instanceof TgUser)) {
            throw new IllegalArgumentException("Provided user is not from TG!");
        }
        repository.save((TgUser) user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return repository.findById(id).map(Function.identity());
    }

    @Override
    public List<Object[]> findByFoodPost(FoodPostDto post) {
        GeoPoint point = post.getPoint();
        List<Long> collect = post.getProducts().stream().map(ProductDto::getId).collect(Collectors.toList());
        return repository.findByProductAndSuitablePlace(collect, point.getLat(), point.getLon());
    }

    @Override
    public Optional<? extends User> getUserWithProducts(Long id) {
        return repository.findWithProductsById(id);
    }
}
