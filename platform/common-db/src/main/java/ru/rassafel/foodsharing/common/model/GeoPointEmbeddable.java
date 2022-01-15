package ru.rassafel.foodsharing.common.model;

import lombok.Data;

import javax.persistence.Embeddable;

/**
 * @author rassafel
 */
@Embeddable
@Data
public class GeoPointEmbeddable {
    private double lat;

    private double lon;
}
