package ru.rassafel.foodsharing.session.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SessionUtil {
    public static Set<String> findValuesByMessage(Map<Integer, String> objectMap, String message) {
        Matcher matcher = Pattern.compile("^\\s*(\\d+\\s*,?\\s*)+$").matcher(message);
        if (!matcher.matches()) {
            Optional<String> first = objectMap.values().stream().filter(entry -> entry.equalsIgnoreCase(message)).findFirst();
            if (first.isEmpty()) {
                throw new IllegalArgumentException(String.format("Названия %s в списке нет, повторите попытку", message));
            }
            return Set.of(first.get());
        }
        Pattern digitPattern = Pattern.compile("\\d+");
        matcher.usePattern(digitPattern);
        matcher.reset();

        Set<Integer> nums = new HashSet<>();
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group());
            if (!objectMap.containsKey(num)) {
                throw new IllegalArgumentException(String.format("Номера %d в списке нет, повторите попытку", num));
            }
            nums.add(num);
        }

        return objectMap.entrySet().stream().filter(entry -> nums.contains(entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toSet());
    }
}
