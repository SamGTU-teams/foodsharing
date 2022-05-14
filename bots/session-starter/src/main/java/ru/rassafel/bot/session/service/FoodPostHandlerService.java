package ru.rassafel.bot.session.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodPostHandlerService {

    private final UserService userService;
    private final Messenger messenger;

    public void handleFoodPostReceived(FoodPostDto foodPostDto){

        class ProductsPlacesEntry {
            private final Set<String> productNames = new HashSet<>();
            private final Set<String> placesNames = new HashSet<>();

            public void addEntry(String productName, String placeName){
                productNames.add(productName);
                placesNames.add(placeName);
            }
        }

        List<Object[]> data = userService.findByFoodPost(foodPostDto);

        Map<Long, ProductsPlacesEntry> grouped = data.stream().collect(Collectors.toMap(
            array -> ((BigInteger) array[0]).longValue(),
            array -> {
                ProductsPlacesEntry entry = new ProductsPlacesEntry();
                entry.addEntry((String) array[2], (String) array[1]);
                return entry;
            },
            (oldEntry, newEntry) -> {
                oldEntry.placesNames.addAll(newEntry.placesNames);
                oldEntry.productNames.addAll(newEntry.productNames);
                return oldEntry;
            }));
        grouped.forEach((key, value) -> {
            String productNames = String.join(",\n", value.productNames);
            String placesNames = String.join(",\n", value.placesNames);
            String resultMessage = String.format("Рядом с вашими местами\n" +
                "%s\n" +
                "и вашими избранными продуктами\n" +
                "%s\n" +
                "Мы нашли пост\n" +
                "%s\n\n" +
                "%s", placesNames, productNames, foodPostDto.getUrl(), foodPostDto.getText());
            messenger.send(resultMessage, key.intValue());
        });
    }

}
