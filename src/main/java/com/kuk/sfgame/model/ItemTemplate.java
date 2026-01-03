package com.kuk.sfgame.model;


import jakarta.persistence.*;


@Entity
@Table(name = "item_templates")
public class ItemTemplate {

    @Id
    private Integer id;  // matches INT in DB

    @Column(name = "name")
    private String name;

    @Column(name = "icon")
    private String icon;

    @Enumerated(EnumType.STRING)
    private ItemSlot slot;  // assuming ItemSlot is an Enum
}
