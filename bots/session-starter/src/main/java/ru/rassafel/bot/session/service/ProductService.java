package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    public List<String> getSimilarProducts(String product) {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("Молоко");
        objects.add("Кефир");
        objects.add("Яблоко");
        return objects;
    }

    public Map<Integer, String> getUsersProductNamesMap(User user) {
        int[] productCounter = {1};
        return user.getProducts().stream().map(Product::getName).collect(Collectors.toMap(
            o -> productCounter[0]++,
            o -> o
        ));
    }

    public List<String> getUsersProductNames(User user) {
        return user.getProducts().stream().map(Product::getName).collect(Collectors.toList());
    }

    public String getUsersProductNamesMapMessage(User user, String additionalMessage) {
        return getUsersProductNamesMap(user).entrySet().stream().map(entry -> entry.getKey() + "." + entry.getValue())
            .collect(Collectors.joining("\n"))
            + "\n\n" + additionalMessage;
    }

    public String getUsersProductNamesMapMessage(User user) {
        return getUsersProductNamesMapMessage(user, "");
    }
}
