package ru.rassafel.bot.session.model.entity.user;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import javax.persistence.*;
import java.util.Collection;

@Data
@MappedSuperclass
public abstract class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "sessionName",
            column = @Column(name = "session_name", nullable = false, length = 63)),
        @AttributeOverride(name = "sessionStep",
            column = @Column(name = "session_step", nullable = false)),
        @AttributeOverride(name = "sessionActive",
            column = @Column(name = "session_active", nullable = false))
    })
    private EmbeddedUserSession userSession = EmbeddedUserSession.EMPTY;

    public abstract void addProduct(Product product);

    public abstract Collection<Product> getProducts();
}
