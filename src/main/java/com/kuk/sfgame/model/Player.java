package com.kuk.sfgame.model;

import com.kuk.sfgame.util.Constants;

import org.apache.tomcat.util.bcel.classfile.Constant;

import io.vavr.collection.List.Cons;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "players")
@Data // Generuje gettery, settery, toString, equals a hashCode
@NoArgsConstructor // Generuje bezparametrický konstruktor
@AllArgsConstructor // Generuje konstruktor se všemi parametry
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "lvl")
    private int level;

    @Column(name = "xp")
    private int experience;

    @Column(name = "gold")
    private int gold;

    @Column(name = "base_strength")
    private int strength;

    @Column(name = "base_constitution")
    private int constitution;

    @Column(name = "base_luck")
    private int luck;

    @Column(name = "energy")
    private Integer energy;

    @Transient
    private int position; // Used for legacy leaderboard display, not persisted to database

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guild_id") // sloupec v tabulce players
    @ToString.Exclude
    private Guild guild;

    @OneToOne(mappedBy = "player", fetch = FetchType.LAZY)
    private LegacyLeaderboard leaderboard;

    @Transient
    private Equipment equipment; // Used to hold player's items, not persisted to database

    @Transient
    private Weapon weapon; // Used to hold player's weapon, not persisted to database

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Quest quest; // Used to hold player's quests, not persisted to database

    public StatsStruct getAllStats() {
        if (equipment == null) {
            return getBaseStats();
        }
        return equipment.getTotalStats()
                        .add(this);         // Add player's base stats
    }

    public StatsStruct getBaseStats() {
        return new StatsStruct(
                strength,
                constitution,
                luck
        );
    }

    public void earnExperience(int xp) {
        this.experience += xp;
        if (this.experience >= Constants.EXPERIENCE_TO_LVLUP[this.level]) {
            this.experience -= Constants.EXPERIENCE_TO_LVLUP[this.level];
            this.level++;
            
            earnExperience(0); // Check for multiple level-ups
        }
    }


    public void earnGold(int gold) {
        this.gold += gold;
    }

    public void spendGold(int gold) {
        if (this.gold >= gold) {
            this.gold -= gold;
        } else {
            throw new IllegalArgumentException("Not enough gold");
        }
    }
}
