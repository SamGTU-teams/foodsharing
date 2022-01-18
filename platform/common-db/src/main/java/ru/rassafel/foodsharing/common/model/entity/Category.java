package ru.rassafel.foodsharing.common.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "public", name = "category",
    uniqueConstraints = @UniqueConstraint(name = "uq_category_name", columnNames = "name"))
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 63)
    private String name;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Product> products;

}
