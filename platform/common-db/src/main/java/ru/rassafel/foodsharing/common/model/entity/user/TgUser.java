package ru.rassafel.foodsharing.common.model.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.rassafel.foodsharing.common.model.entity.geo.TgUserPlace;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "tg_bot", name = "user")
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class TgUser extends User{

    @ManyToMany
    @JoinTable(schema = "tg_bot", name = "user_products",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Collection<Product> products = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private Collection<TgUserPlace> places;

    @Override
    public void addProduct(Product product) {
        products.add(product);
    }


}
