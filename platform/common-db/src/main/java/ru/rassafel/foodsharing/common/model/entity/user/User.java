package ru.rassafel.foodsharing.common.model.entity.user;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import javax.persistence.*;
import java.util.Collection;

@Data
@MappedSuperclass
public abstract class User {

    @Id
    private Long id;
    private EmbeddedUserSession userSession = EmbeddedUserSession.EMPTY;

    public abstract void addProduct(Product product);

    public abstract Collection<Product> getProducts();

}
