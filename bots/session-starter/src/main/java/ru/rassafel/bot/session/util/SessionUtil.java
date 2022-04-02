package ru.rassafel.bot.session.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SessionUtil {

    public static Set<String> getAllNames(Map<Integer, String> objectMap, String message){
        Matcher matcher = Pattern.compile("(\\d+\\s*,?)+").matcher(message);
        if(!matcher.matches()){
            if(objectMap.entrySet().stream().noneMatch(entry -> entry.getValue().equalsIgnoreCase(message))){
                throw new IllegalArgumentException("Введено неверное название, попробуйте еще");
            }
            return Set.of(message);
        }
        Pattern digitPattern = Pattern.compile("\\d+");
        matcher.usePattern(digitPattern);
        matcher.reset();

        List<Integer> nums = new ArrayList<>();
        while (matcher.find()){
            int num = Integer.parseInt(matcher.group());
            if(!objectMap.containsKey(num)){
                throw new IllegalArgumentException(String.format("Номера %d в списке нет, повторите попытку", num));
            }
            nums.add(num);
        }

        return objectMap.entrySet().stream().filter(entry -> nums.contains(entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toSet());
    }

}
