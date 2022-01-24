package ru.rassafel.foodsharing.parser.model.dto;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rassafel
 */
@Data
public class RawPostDto {
    private String url;

    private LocalDateTime date;

    private String text;

    private PostContext context;
}
