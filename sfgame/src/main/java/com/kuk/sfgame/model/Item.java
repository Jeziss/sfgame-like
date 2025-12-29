package com.kuk.sfgame.model;

import jakarta.persistence.*;

//@Entity
//@Table(name = "item_templates")
public class Item {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@Column(name = "name")
    private String name;
    private int constitution;
    private int strength;
    private int dexterity;
    private int intelligence;


    public Item(int id, String name, String description, int level, int experience, int gold, int constitution, int strength, int dexterity, int intelligence) {
        this.id = id;
        this.name = name;
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
    
}
