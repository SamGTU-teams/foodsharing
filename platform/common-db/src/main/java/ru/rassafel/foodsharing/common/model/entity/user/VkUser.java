package ru.rassafel.foodsharing.common.model.entity.user;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.foodsharing.common.model.entity.geo.TgUserPlace;
import ru.rassafel.foodsharing.common.model.entity.geo.VkUserPlace;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(schema = "vk_bot", name = "user")
@Data
public class VkUser extends User{

    @ManyToMany
    @JoinTable(schema = "vk_bot", name = "user_products",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Collection<Product> products = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private Collection<VkUserPlace> places;

    @Override
    public void addProduct(Product product) {
        products.add(product);
    }

}
