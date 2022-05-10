package ru.rassafel.bot.session.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class OpenStreetMapService {
    private static final String OPEN_MAP_ROOT_PATH =
        "https://nominatim.openstreetmap.org/reverse?lat={lat}&lon={lon}&format={format}&accept-language={lang}";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String getAddress(double lat, double lon) {
        String response = restTemplate.getForObject(OPEN_MAP_ROOT_PATH, String.class,
            lat,
            lon,
            "geojson",
            "ru");
        try {
            FeatureCollection allFeatures = objectMapper.readValue(response, FeatureCollection.class);
            List<Feature> features = allFeatures.getFeatures();
            if (features.isEmpty()) {
                return "Место не распознано";
            }
            Feature feature = features.get(0);
            Map<String, Object> properties = feature.getProperties();
            if (properties.isEmpty()) {
                return "Место не распознано";
            }

            Map<String, String> address = ofNullable(properties.get("address"))
                .map(map -> (Map<String, String>) map)
                .orElseThrow();

            List<String> addressParts = new LinkedList<>();


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return response;
    }

}
