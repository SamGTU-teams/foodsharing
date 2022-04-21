package ru.rassafel.foodsharing.analyzer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;
import ru.rassafel.foodsharing.analyzer.service.GeoLuceneAnalyzerService;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class GeoLuceneAnalyzerServiceImpl implements GeoLuceneAnalyzerService {
    private final LuceneRepository repository;

    @Override
    public Optional<GeoPoint> parseGeoPoint(RawPost post) {
        return Optional.ofNullable(post.getContext().getPoint());
    }

    @Override
    public Optional<GeoPoint> parseGeoPoint(String... strings) {
        return Optional.empty();
    }

    @Override
    public Optional<GeoPoint> parseGeoPoint(RawPost post, LuceneIndexedString... strings) {
        Optional<GeoPoint> geoPoint = parseGeoPoint(post);
        if (geoPoint.isPresent()) return geoPoint;
        return parseGeoPoint(strings);
    }

    @Override
    public Optional<GeoPoint> parseGeoPoint(LuceneIndexedString... strings) {
        String[] array = Arrays.stream(strings).map(LuceneIndexedString::getBody)
            .toArray(String[]::new);
        return parseGeoPoint(array);
    }
}
