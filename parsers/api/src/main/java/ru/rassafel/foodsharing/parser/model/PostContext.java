package ru.rassafel.foodsharing.parser.model;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;

import java.util.List;

/**
 * @author rassafel
 */
@Data
public class PostContext {
    private List<String> attachments;

    private GeoPoint point;

    private List<RegionDto> regions;
}
