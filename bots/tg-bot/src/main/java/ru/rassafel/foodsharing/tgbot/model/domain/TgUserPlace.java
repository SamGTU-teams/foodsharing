package ru.rassafel.foodsharing.tgbot.model.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import ru.rassafel.bot.session.model.entity.Place;

import javax.persistence.*;

@Entity
@Table(schema = "tg_bot", name = "tg_place")
@Data
public class TgUserPlace extends Place {
    @Id
    @GeneratedValue(generator = "tg_bot_place_id_seq")
    @GenericGenerator(
        name = "tg_bot_place_id_seq",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "tg_bot.tg_bot_place_id_seq")
        }
    )
    @Column(name = "id", nullable = false)
    private Long id;
}
