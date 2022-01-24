package ru.rassafel.foodsharing.vkparser.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vk.api.sdk.objects.wall.Geo;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import lombok.Data;

import java.util.List;

/**
 * @author rassafel
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallpost {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("owner_id")
    private Integer ownerId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("date")
    private Integer date;

    @JsonProperty("attachments")
    private List<WallpostAttachment> attachments;

    @JsonProperty("geo")
    private Geo geo;
}
