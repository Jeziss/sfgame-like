package com.kuk.sfgame.model;

public class Monster {
    private Long id;
    private String name;
    private int level;
    private int health;
    private int strength;
    private int dexterity;
    private int intelligence;


    public Monster(Long id, String name, int level, int experience, int gold, int health, int strength, int dexterity, int intelligence) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }   
}
