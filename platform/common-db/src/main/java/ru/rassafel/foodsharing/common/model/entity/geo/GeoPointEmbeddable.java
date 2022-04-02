package ru.rassafel.foodsharing.common.model.entity.geo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * @author rassafel
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoPointEmbeddable {
    private double lat;

    private double lon;
}
