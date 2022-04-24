package ru.rassafel.foodsharing.ibot.model.entity;

import lombok.*;
import ru.rassafel.foodsharing.common.model.entity.geo.GeoPointEmbeddable;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.common.model.entity.geo.Region;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "ibot", name = "food_post")
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class FoodPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "text", nullable = false, length = 1023)
    private String text;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "lat", column = @Column(name = "lat")),
        @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    private GeoPointEmbeddable point;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(schema = "ibot", name = "food_post_regions",
        joinColumns = @JoinColumn(name = "food_post_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_IBOT_FOOD_POST_REGIONS_FOOD_POST_ID")),
        inverseJoinColumns = @JoinColumn(name = "region_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_IBOT_FOOD_POST_REGIONS_REGION_ID")))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Region> regions;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema = "ibot", name = "food_post_products",
        joinColumns = @JoinColumn(name = "food_post_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_IBOT_FOOD_POST_PRODUCTS_FOOD_POST_ID")),
        inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_IBOT_FOOD_POST_PRODUCTS_PRODUCT_ID")))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Product> products;
}
