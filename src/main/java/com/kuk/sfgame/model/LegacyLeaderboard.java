package com.kuk.sfgame.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "legacy_leaderboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegacyLeaderboard {

    @Id
    @Column(name = "player_id")
    private int playerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "position")
    private int position;
}
