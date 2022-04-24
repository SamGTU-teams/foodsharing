package ru.rassafel.bot.session.model.entity.place;

import lombok.Data;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.geo.GeoPointEmbeddable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class Place {
    private String name;
    private Integer radius;
    @Embedded
    private GeoPointEmbeddable geo;
    @Column(name = "user_id")
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
