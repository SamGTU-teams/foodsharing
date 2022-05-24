package ru.rassafel.foodsharing.tgbot.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.tgbot.model.domain.TgUser;
import ru.rassafel.foodsharing.tgbot.repository.TgUserRepository;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public Optional<? extends User> getUser(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<UserPlacesAndProducts> findByFoodPost(FoodPostDto post) {
        GeoPoint point = post.getPoint();
        List<Long> products = post.getProducts().stream()
            .map(ProductDto::getId)
            .collect(Collectors.toList());

        return repository.findByProductAndSuitablePlace(products, point.getLat(), point.getLon()).
            stream().collect(Collectors.toMap(
                array -> ((BigInteger) array[0]).longValue(),
                array -> {
                    Pair<HashSet<String>, HashSet<String>> pair
                        = Pair.of(new HashSet<>(), new HashSet<>());
                    pair.getLeft().add((String) array[1]); // place
                    pair.getRight().add((String) array[2]); // product
                    return pair;
                },
                (oldEntry, newEntry) -> {
                    oldEntry.getRight().addAll(newEntry.getRight());
                    oldEntry.getLeft().addAll(newEntry.getLeft());
                    return oldEntry;
                })).entrySet().stream()
            .map(entry -> new UserPlacesAndProducts(entry.getKey(), entry.getValue().getLeft(), entry.getValue().getRight()))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends User> getUserWithProducts(Long id) {
        return repository.findWithProductsById(id);
    }
}
