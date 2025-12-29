package com.kuk.sfgame.model;

import java.util.Comparator;
import java.util.List;

import java.util.Collections;

public class Guild {
    
    String name;

    // Members sorted by level descending
    List<Player> members;



    public Guild(String name, List<Player> members) {
        this.name = name;
        this.members = members;

        // Sort members by level in descending order
        Collections.sort(members, new Comparator<Player>() {
        @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(-p1.getLevel(), -p2.getLevel());
            }
        });
    }

    public String getName() {
        return name;
    }






}
