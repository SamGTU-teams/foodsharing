package ru.rassafel.bot.session.templates;

import lombok.RequiredArgsConstructor;
import ru.rassafel.bot.session.model.entity.Place;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum PlaceTemplates implements Templates {
    CHOOSE_GEO_OPERATION("places/choose-geo-operation"),
    EXPECTATION_OF_GEO("places/expectation-of-geo"),
    EXPECTATION_OF_GEO_NAME("places/expectation-of-geo-name"),
    EXPECTATION_OF_RADIUS("places/expectation-of-radius"),
    EXPECTATION_OF_NEW_RADIUS("places/expectation-of-new-radius"),
    PLACE_EDIT_SUCCESS("places/place-edit-success"),
    FINISHED_SAVE_POINT("places/finish-save-point"),
    INVALID_RADIUS_RANGE("places/invalid-radius-range"),
    PLACES_LIST_TO_DELETE("places/list-of-places-to-delete"),
    PLACES_LIST("places/list-of-places"),
    EMPTY_PLACES_AFTER_DELETE("places/empty-places-after-delete"),
    PLACES_LIST_AFTER_DELETE("places/list-of-places-after-delete"),
    EMPTY_PLACES("places/empty-places-list"),
    NO_GEOLOCATION_PROVIDED("places/no-geolocation-provided"),
    PLACES_LIST_TO_EDIT("places/list-of-places-to-edit"),
    TOO_MANY_PLACE_NAME("places/too-many-place-name"),
    TOO_MANY_PLACES("places/too-many-places"),
    PLACE_ALREADY_EXISTS("places/place-already-exists"),
    INVALID_RADIUS_FORMAT("places/invalid-radius-format"),
    PLACE_WITH_NAME_NOT_FOUND("places/place-with-name-not-found"),
    PLACE_BY_NUM_NOT_FOUND("places/place-by-num-not-found");

    private final String name;

    public static Map<String, Object> buildMapOfPlaces(Collection<Place> places) {
        AtomicInteger indexer = new AtomicInteger(1);
        return Map.of("places",
            places.stream().map(place ->
                Map.of(
                    "index", indexer.getAndIncrement(),
                    "name", place.getName(),
                    "radius", place.getRadius(),
                    "address", place.getAddress()
                )).collect(Collectors.toList()));
    }

    @Override
    public String getName() {
        return getName(name);
    }
}
