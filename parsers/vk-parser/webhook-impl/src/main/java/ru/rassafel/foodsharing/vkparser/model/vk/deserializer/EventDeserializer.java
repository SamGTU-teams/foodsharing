package ru.rassafel.foodsharing.vkparser.model.vk.deserializer;

import com.vk.api.sdk.events.Events;
import org.springframework.boot.jackson.JsonComponent;

/**
 * @author rassafel
 */
@JsonComponent
public class EventDeserializer extends EnumDeserializer<Events> {
    public EventDeserializer() {
        super(Events.class);
    }
}
