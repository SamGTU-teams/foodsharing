package ru.rassafel.foodsharing.vkparser.model.vk.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * @author rassafel
 */
public abstract class EnumDeserializer<T extends Enum<T>> extends StdDeserializer<T> {
    protected EnumDeserializer(Class<T> vc) {
        super(vc);
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return Enum.valueOf((Class<T>) handledType(), parser.getText().toUpperCase());
    }
}
