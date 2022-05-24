package ru.rassafel.foodsharing.session.service.openmap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "open-street-map-service.stub", havingValue = "true")
@Primary
public class OpenStreetMapAddressServiceStub implements AddressService {
    @Override
    public String getAddress(Double lat, Double lon) {
        return "415964, Амурская область, город Ступино, шоссе Космонавтов, 56";
    }
}
