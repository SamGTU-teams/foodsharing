package ru.rassafel.bot.session.dto;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.PlatformType;

@Data
public class SessionRequest {

    private String message;
    private From from;
    private LocationDto location;

    private PlatformType type;

    public SessionRequest type(PlatformType type){
        this.type = type;
        return this;
    }


}
