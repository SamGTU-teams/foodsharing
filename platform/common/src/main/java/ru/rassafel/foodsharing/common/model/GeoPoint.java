package ru.rassafel.foodsharing.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GeoPoint {
    private double lat;

    private double lon;
}
