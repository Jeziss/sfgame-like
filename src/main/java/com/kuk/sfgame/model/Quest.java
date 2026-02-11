package com.kuk.sfgame.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String location;

    @Column(name = "energy_cost", nullable = false)
    private int energyCost;

    @Column(name = "xp_reward", nullable = false)
    private int xpReward;

    @Column(name = "gold_reward", nullable = false)
    private int goldReward;

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;
}
