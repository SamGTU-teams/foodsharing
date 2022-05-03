package ru.rassafel.foodsharing.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.GeoPoint;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RegionDto {
    private Long id;

    private String name;

    private GeoPoint point;
}
