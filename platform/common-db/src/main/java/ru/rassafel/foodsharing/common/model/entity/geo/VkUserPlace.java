package ru.rassafel.foodsharing.common.model.entity.geo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import ru.rassafel.foodsharing.common.model.entity.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(schema = "vk_bot", name = "place")
@Data
public class VkUserPlace extends Place{


    @Id
    @GeneratedValue(generator = "vk_bot_place_id_seq")
    @GenericGenerator(
        name = "vk_bot_place_id_seq",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "vk_bot.place_id_sequence")
        }
    )
    private Long id;
}
