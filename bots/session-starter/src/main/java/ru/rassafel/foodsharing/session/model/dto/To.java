package ru.rassafel.foodsharing.session.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class To {

    public To(Long id) {
        this.id = id;
    }

    public To(List<Long> userIds) {
        this.userIds = userIds;
    }

    private Long id;
    private List<Long> userIds;
}
