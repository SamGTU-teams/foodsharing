package ru.rassafel.foodsharing.parser.model;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * @author rassafel
 */
@Data
public class PostContext {
    @NotNull
    private List<String> attachments;

    @Null
    private GeoPoint point;

    @NotNull
    private List<RegionDto> regions;
}
