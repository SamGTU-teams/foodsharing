package ru.rassafel.bot.session.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.GeoPoint;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SessionRequest {
    private String message;
    private From from;
    private GeoPoint location;
}
