package ru.rassafel.foodsharing.common.model.entity;

import lombok.*;
import ru.rassafel.foodsharing.common.model.GeoPointEmbeddable;

import javax.persistence.*;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "public", name = "region",
    uniqueConstraints = @UniqueConstraint(name = "uq_region_name", columnNames = "name"))
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "lat", column = @Column(name = "lat")),
        @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    private GeoPointEmbeddable point;

    @Column(name = "name", nullable = false, length = 63)
    private String name;
}
