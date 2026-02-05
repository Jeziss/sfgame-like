package com.kuk.sfgame.model;

/**
 * Simple value object representing combined character statistics.
 *
 * The game now uses three core stats:
 * - strength
 * - constitution
 * - luck
 */
public record StatsStruct(
        int strength,
        int constitution,
        int luck
) {

    public StatsStruct add(Item item) {
        return new StatsStruct(
                strength + (item.getStrength() != null ? item.getStrength() : 0),
                constitution + (item.getConstitution() != null ? item.getConstitution() : 0),
                luck + (item.getLuck() != null ? item.getLuck() : 0)
        );
    }

    public StatsStruct add(Player player) {
        return new StatsStruct(
                strength + player.getStrength(),
                constitution + player.getConstitution(),
                luck + player.getLuck()
        );
    }

    public StatsStruct add(StatsStruct other) {
        return new StatsStruct(
                strength + other.strength(),
                constitution + other.constitution(),
                luck + other.luck()
        );
    }
}