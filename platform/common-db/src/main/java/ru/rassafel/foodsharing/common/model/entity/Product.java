package ru.rassafel.foodsharing.common.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "public", name = "product",
    uniqueConstraints = @UniqueConstraint(name = "uq_product_name", columnNames = "name"))
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 63)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id",
        foreignKey = @ForeignKey(name = "fk_product_category_category"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;
}
