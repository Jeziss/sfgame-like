package com.kuk.sfgame.model;

public record StatsStruct(
        int constitution,
        int strength,
        int dexterity,
        int intelligence
) {

    public StatsStruct add(Item item) {
        return new StatsStruct(
                constitution + item.getConstitution(),
                strength + item.getStrength(),
                dexterity + item.getDexterity(),
                intelligence + item.getIntelligence()
        );
    }

    public StatsStruct add(Player player) {
        return new StatsStruct(
                constitution + player.getConstitution(),
                strength + player.getStrength(),
                dexterity + player.getDexterity(),
                intelligence + player.getIntelligence()
        );
    }

    public StatsStruct add(StatsStruct allStat) {
        return new StatsStruct(
                constitution + allStat.constitution(),
                strength + allStat.strength(),
                dexterity + allStat.dexterity(),
                intelligence + allStat.intelligence()
        );
    }
}