package ru.rassafel.foodsharing.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rassafel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPoint {
    private double lat;

    private double lon;
}
