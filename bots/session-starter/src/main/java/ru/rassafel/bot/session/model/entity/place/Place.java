package ru.rassafel.bot.session.model.entity.place;

import lombok.Data;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.geo.GeoPointEmbeddable;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class Place {
    @Column(name = "name", nullable = false, length = 63)
    private String name;
    @Column(name = "radius", nullable = false)
    private Integer radius = 1000;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "lat",
            column = @Column(name = "lat", nullable = false)),
        @AttributeOverride(name = "lon",
            column = @Column(name = "lon", nullable = false))
    })
    private GeoPointEmbeddable geo;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Place withName(String name) {
        this.name = name;
        return this;
    }

    public Place withRadius(Integer radius) {
        this.radius = radius;
        return this;
    }

    public Place withGeo(GeoPointEmbeddable geo) {
        this.geo = geo;
        return this;
    }

    public Place withUser(User user) {
        this.userId = user.getId();
        return this;
    }
}
