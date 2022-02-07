package ru.rassafel.foodsharing.tgbot.model.entity;

import lombok.*;
import ru.rassafel.foodsharing.common.model.entity.Product;

import javax.persistence.*;
import java.util.Set;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "tg_bot", name = "user")
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class TgUser {
    @Id
    @Column(name = "id")
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "active", column = @Column(name = "session_active", nullable = false)),
        @AttributeOverride(name = "step", column = @Column(name = "session_step", nullable = false)),
        @AttributeOverride(name = "name", column = @Column(name = "session_name", nullable = false, length = 63))
    })
    private TgUserSession session;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TgUserPlace> places;

    @ManyToMany
    @JoinTable(schema = "tg_bot", name = "user_products",
        joinColumns = @JoinColumn(name = "user_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "products_id", nullable = false),
        foreignKey = @ForeignKey(name = "fk_tg_user_products_user"),
        inverseForeignKey = @ForeignKey(name = "fk_tg_user_products_product"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Product> products;
}
