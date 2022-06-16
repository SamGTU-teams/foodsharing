package ru.rassafel.foodsharing.analyzer.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.parser.model.PostContext;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class GeoLuceneAnalyzerServiceImplTest {
    GeoLuceneAnalyzerServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GeoLuceneAnalyzerServiceImpl(null);
    }

    @Test
    void parseGeoPoint() {
        double lat = 10;
        double lon = 20;

        GeoPoint geoPoint = new GeoPoint(lat, lon);
        GeoPoint expected = new GeoPoint(lat, lon);

        PostContext context = new PostContext();
        context.setAttachments(List.of());
        context.setPoint(geoPoint);

        RawPostDto post = new RawPostDto();
        post.setDate(LocalDateTime.now());
        post.setText("Test string");
        post.setUrl("test.url");
        post.setContext(context);

        Optional<GeoPoint> actual = service.parseGeoPoint(post);

        assertThat(actual)
            .isNotEmpty();

        assertThat(actual.get())
            .isNotSameAs(expected)
            .isEqualTo(expected);
    }
}
