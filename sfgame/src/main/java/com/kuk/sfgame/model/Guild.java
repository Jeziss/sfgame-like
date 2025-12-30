package com.kuk.sfgame.model;

import java.util.Comparator;
import java.util.List;

import jakarta.persistence.*;


import java.util.Collections;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "guilds")
@Data // Generuje gettery, settery, toString, equals a hashCode
@NoArgsConstructor // Generuje bezparametrický konstruktor
@AllArgsConstructor // Generuje konstruktor se všemi parametry
public class Guild {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    String name;

    @Transient
    List<Player> members;

    @Column(name = "xp_bonus_percent")
    int xpBonusPercent;

    @Column(name = "gold_bonus_percent")
    int goldBonusPercent;

    @Column(name = "dmg_bonus_percent")
    int dmgBonusPercent;

    @Column(name = "hp_bonus_percent")
    int hpBonusPercent;

    public GuildBonus getGuildBonus() {
        return new GuildBonus(goldBonusPercent, xpBonusPercent, hpBonusPercent, dmgBonusPercent);
    }
}
