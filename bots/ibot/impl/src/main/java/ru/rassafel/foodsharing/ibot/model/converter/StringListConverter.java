package ru.rassafel.foodsharing.ibot.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author rassafel
 */
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return String.join(" ", strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return new ArrayList<>(Arrays.asList(s.split("\\s+")));
    }
}
