package ru.rassafel.foodsharing.vkparser.model.dto;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;

import java.util.Date;
import java.util.List;

/**
 * @author rassafel
 */
@Data
public class RawPostDto {
    private String url;

    private Date date;

    private String text;

    private List<String> attachments;

    private GeoPoint point;

    private List<RegionDto> regions;
}
