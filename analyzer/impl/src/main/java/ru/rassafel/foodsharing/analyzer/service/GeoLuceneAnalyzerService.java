package ru.rassafel.foodsharing.analyzer.service;

import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.Optional;

/**
 * @author rassafel
 */
public interface GeoLuceneAnalyzerService extends GeoAnalyzerService {
    Optional<GeoPoint> parseGeoPoint(RawPost post, LuceneIndexedString... strings);

    Optional<GeoPoint> parseGeoPoint(LuceneIndexedString... strings);
}
