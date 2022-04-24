package ru.rassafel.foodsharing.common.model.entity.geo;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.entity.user.User;

import javax.persistence.*;

@Entity
@Table(schema = "vk_bot", name = "place")
@Data
public class VkUserPlace extends Place{


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vk_bot_place_id_seq")
    @SequenceGenerator(schema = "vk_bot", sequenceName = "place_id_sequence", name = "vk_bot_place_id_seq")
    private Long id;
}
