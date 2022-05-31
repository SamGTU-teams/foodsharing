package ru.rassafel.foodsharing.session.service.openmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rassafel.foodsharing.session.service.AddressService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenStreetMapAddressService implements AddressService {
    private static final String OPEN_MAP_ROOT_PATH =
        "https://nominatim.openstreetmap.org/reverse?lat={lat}&lon={lon}&format={format}&accept-language={lang}";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String getAddress(Double lat, Double lon) {
        String resultName = "Не определено";
        String response;
        try {
            response = restTemplate.getForObject(OPEN_MAP_ROOT_PATH, String.class,
                lat,
                lon,
                "geojson",
                "ru");
        } catch (Exception ex) {
            log.warn("Place name not found by coordinates : {}, {}", lat, lon);
            return resultName;
        }


        JsonNode root;
        try {
            root = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            log.warn("Place name not found by coordinates : {}, {}", lat, lon);
            return resultName;
        }
        JsonNode features = root.get("features");
        if (features == null || !features.isArray()) {
            log.warn("Place name not found by coordinates : {}, {}", lat, lon);
            return resultName;
        }
        if (features.isEmpty()) {
            return resultName;
        }
        JsonNode firstFeature = features.get(0);
        if (firstFeature != null) {
            JsonNode properties = firstFeature.get("properties");
            if (properties != null) {
                JsonNode name = properties.get("display_name");
                if (name == null) {
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
