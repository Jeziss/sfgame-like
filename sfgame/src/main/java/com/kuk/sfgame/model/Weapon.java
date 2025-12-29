package com.kuk.sfgame.model;

public class Weapon extends Item {
    private int minDamage;
    private int maxDamage;

    public Weapon(int id, String name, String description, int level, int experience, int gold, int constitution, int strength, int dexterity, int intelligence, int minDamage, int maxDamage) {
        super(id, name, description, level, experience, gold, constitution, strength, dexterity, intelligence);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }
    
    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }
}
