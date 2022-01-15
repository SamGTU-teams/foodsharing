package ru.rassafel.foodsharing.common.model.dto;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.GeoPoint;

/**
 * @author rassafel
 */
@Data
public class RegionDto {
    private String id;

    private String name;

    private GeoPoint point;
}
