package ru.rassafel.foodsharing.vkbot.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vk.api.sdk.objects.callback.Type;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class VkTypeDeserializer extends JsonDeserializer<Type> {
    @Override
    public Type deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        return Type.valueOf(text.toUpperCase());
    }
}
