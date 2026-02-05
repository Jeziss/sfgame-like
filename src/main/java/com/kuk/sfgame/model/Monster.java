package com.kuk.sfgame.model;

public class Monster {
    private Long id;
    private String name;
    private int level;
    private int health;
    private int strength;
    private int constitution;
    private int luck;


    public Monster(Long id, String name, int level, int experience, int gold, int health, int strength, int constitution, int luck) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.constitution = constitution;
        this.luck = luck;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }   
}
