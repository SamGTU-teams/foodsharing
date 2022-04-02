package ru.rassafel.foodsharing.common.model.entity.geo;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(schema = "tg_bot", name = "place")
@Data
public class TgUserPlace extends Place {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tg_bot_place_id_seq")
    @SequenceGenerator(schema = "tg_bot", sequenceName = "place_id_sequence", name = "tg_bot_place_id_seq")
    private Long id;

}
