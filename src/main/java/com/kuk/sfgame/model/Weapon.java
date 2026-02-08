package com.kuk.sfgame.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "weapon_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weapon {

    @Id
    @Column(name = "player_item_id")
    private Integer id;

    @Column(name = "min_damage")
    private Integer minDamage;

    @Column(name = "max_damage")
    private Integer maxDamage;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "player_item_id")
    @ToString.Exclude
    private Item playerItem;
}
