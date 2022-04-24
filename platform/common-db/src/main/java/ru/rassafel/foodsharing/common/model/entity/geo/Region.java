package ru.rassafel.foodsharing.common.model.entity.geo;

import lombok.*;

import javax.persistence.*;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "public", name = "region",
    uniqueConstraints = @UniqueConstraint(name = "UQ_REGION_NAME", columnNames = "name"))
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