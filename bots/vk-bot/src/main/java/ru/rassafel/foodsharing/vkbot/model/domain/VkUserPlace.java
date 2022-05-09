package ru.rassafel.foodsharing.vkbot.model.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import ru.rassafel.bot.session.model.entity.Place;

import javax.persistence.*;

@Entity
@Table(schema = "vk_bot", name = "vk_place")
@Data
public class VkUserPlace extends Place {
    @Id
    @GeneratedValue(generator = "vk_bot_place_id_seq")
    @GenericGenerator(
        name = "vk_bot_place_id_seq",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "vk_bot.vk_bot_place_id_seq")
        }
    )
    @Column(name = "id", nullable = false)
    private Long id;
}
