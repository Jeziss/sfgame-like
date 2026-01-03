package com.kuk.sfgame.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "base_constitution")
    private int constitution;

    @Column(name = "base_strength")
    private int strength;

    @Column(name = "base_dexterity")
    private int dexterity;

    @Column(name = "base_intelligence")
    private int intelligence;

    @Column(name = "player_class")
    @Enumerated(EnumType.STRING)
    private PlayerClass playerClass;

    @Transient
    private int position; // Used for legacy leaderboard display, not persisted to database

    @Transient
    private String guild; // Used for guild display, not persisted to database

    @Transient
    private Equipment equipment; // Used to hold player's items, not persisted to database

    @Transient
    private Weapon weapon; // Used to hold player's weapon, not persisted to database

    public StatsStruct getAllStats() {

        return equipment.getTotalStats()
                        .add(this);         // Add player's base stats
    }

    public StatsStruct getBaseStats() {
        return new StatsStruct(
                constitution,
                strength,
                dexterity,
                intelligence
        );
    }

}
