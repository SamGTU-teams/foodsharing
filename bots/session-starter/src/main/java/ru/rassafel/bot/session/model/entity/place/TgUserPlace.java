package ru.rassafel.bot.session.model.entity.place;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
            @Parameter(name = "sequence_name", value = "tg_bot.place_id_sequence")
        }
    )
    private Long id;
}
