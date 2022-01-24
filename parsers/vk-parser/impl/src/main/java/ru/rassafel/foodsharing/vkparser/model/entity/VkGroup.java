package ru.rassafel.foodsharing.vkparser.model.entity;

import lombok.*;
import ru.rassafel.foodsharing.common.model.entity.Region;

import javax.persistence.*;
import java.util.List;

/**
 * @author rassafel
 */
@Entity
@Table(schema = "vk_parser", name = "group")
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class VkGroup {
    @Id
    @Column(name = "id", nullable = false)
    private Integer groupId;

    @Column(name = "server_id")
    private Integer serverId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "confirmation_code", nullable = false)
    private String confirmationCode;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(schema = "vk_parser", name = "group_regions",
        joinColumns = @JoinColumn(name = "group_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "region_id", nullable = false))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Region> regions;
}
