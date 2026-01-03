package com.kuk.sfgame.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "item_templates")
@Data // Generuje gettery, settery, toString, equals a hashCode
@NoArgsConstructor // Generuje bezparametrický konstruktor
@AllArgsConstructor // Generuje konstruktor se všemi parametry
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
