package ru.rassafel.foodsharing.common.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "public", name = "product",
    uniqueConstraints = @UniqueConstraint(name = "UQ_PRODUCT_NAME", columnNames = "name"))
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 63)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id",
        foreignKey = @ForeignKey(name = "FK_PRODUCT_CATEGORY_ID"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;
}
