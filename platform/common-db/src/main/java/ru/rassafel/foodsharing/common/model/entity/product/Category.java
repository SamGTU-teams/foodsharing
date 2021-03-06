package ru.rassafel.foodsharing.common.model.entity.product;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "public", name = "category",
    uniqueConstraints = @UniqueConstraint(name = "UQ_CATEGORY_NAME", columnNames = "name"))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 63)
    private String name;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Product> products = new HashSet<>();
}
