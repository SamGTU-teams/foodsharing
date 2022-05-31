package ru.rassafel.foodsharing.analyzer.service;

import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;

import java.util.Optional;

/**
 * @author rassafel
 */
public interface GeoAnalyzerService {
    Optional<GeoPoint> parseGeoPoint(RawPostDto post);

    Optional<GeoPoint> parseGeoPoint(String... strings);
}
