package com.kuk.sfgame.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quest_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private String location;

    @Column(name = "energy_cost", nullable = false)
    private int energyCost;

    @Column(name = "xp_reward", nullable = false)
    private int xpReward;

    @Column(name = "gold_reward", nullable = false)
    private int goldReward;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;
}
