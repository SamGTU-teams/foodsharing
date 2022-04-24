package ru.rassafel.bot.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rassafel.foodsharing.common.model.PlatformType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequest {
    private String message;
    private From from;
    private LocationDto location;

    private PlatformType type;

    public SessionRequest type(PlatformType type) {
        this.type = type;
        return this;
    }
}
