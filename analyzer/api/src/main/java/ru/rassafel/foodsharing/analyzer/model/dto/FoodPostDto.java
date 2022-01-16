package ru.rassafel.foodsharing.analyzer.model.dto;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;

import java.util.Date;
import java.util.List;

/**
 * @author rassafel
 */
@Data
public class FoodPostDto {
    private String url;

    private Date date;

    private String text;

    private List<String> attachments;

    private GeoPoint point;

    private List<RegionDto> regions;

    private List<ProductDto> products;
}
