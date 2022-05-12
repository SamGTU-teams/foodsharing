package ru.rassafel.bot.session.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
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

        String resultName = "Не определено";
        JsonNode root;
        try {
             root = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            log.warn("Place name not found by coordinates : {}, {}", lat, lon);
            return resultName;
        }
        JsonNode features = root.get("features");
        if(features == null || !features.isArray()){
            log.warn("Place name not found by coordinates : {}, {}", lat, lon);
            return resultName;
        }
        if(features.isEmpty()){
            return resultName;
        }
        JsonNode firstFeature = features.get(0);
        if(firstFeature != null){
            JsonNode properties = firstFeature.get("properties");
            if(properties != null){
                JsonNode name = properties.get("display_name2");
                if(name == null){
                    return resultName;
                }
                List<String> splitted = Arrays.stream(name.asText().split(","))
                    .map(String::trim).collect(Collectors.toList());
                Collections.reverse(splitted);
                resultName = String.join(", ", splitted);
            }
        }
        return resultName;
    }

}
