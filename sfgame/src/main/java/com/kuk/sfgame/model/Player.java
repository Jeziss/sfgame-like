package com.kuk.sfgame.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
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

    public Player() {
    }

    public Player(int id, String name, int level, int experience, int gold, int constitution, int strength, int dexterity, int intelligence) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.gold = gold;
        this.constitution = constitution;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }


    
}
