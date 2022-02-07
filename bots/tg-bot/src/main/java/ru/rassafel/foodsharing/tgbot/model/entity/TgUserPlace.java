package ru.rassafel.foodsharing.tgbot.model.entity;

import lombok.*;
import ru.rassafel.foodsharing.common.model.GeoPointEmbeddable;

import javax.persistence.*;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "tg_bot", name = "place",
    uniqueConstraints = @UniqueConstraint(name = "uq_tg_place_name_client_id", columnNames = {"name", "user_id"}))
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class TgUserPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 63)
    private String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "lat", column = @Column(name = "lat", nullable = false)),
        @AttributeOverride(name = "lon", column = @Column(name = "lon", nullable = false))
    })
    private GeoPointEmbeddable point;

    @Column(name = "radius", nullable = false)
    private Integer radius;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_tg_place_user"))
    private TgUser user;
}
