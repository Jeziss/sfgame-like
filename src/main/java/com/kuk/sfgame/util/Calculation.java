package com.kuk.sfgame.util;

import java.util.Random;

import com.kuk.sfgame.util.Constants;

// All calculations have been reverse engineered from the game help tool behaviour:
// https://sftools.mar21.eu/attributes.html

// Todo:
// - Add more complex calculations (e.g. damage, defense, etc.) based on the stats
// - Add methods to calculate derived stats (e.g. HP, attack power, etc.) based on the core stats
// - Calculate the amount of xp and gold received from the tavern - DONE
// - Calculate price of the item
// - Calculate the stats of the item based on level - DONE
// - Calculate the price of attribute upgrade - DONE


public final class Calculation {

    private static final Random RANDOM = new Random();

    private Calculation() {
        // Private constructor to prevent instantiation
    }

    // --------------- Tavern calculations XP ---------------

    private static int calculateBaseXP(int playerLevel) {
        return (int) Math.floor(calculateXPForLevelUp(playerLevel) / experienceMultiplier(playerLevel));
    }
    
    private static double experienceMultiplier(int playerLevel) {
        return 0.75 * Math.max(0, playerLevel + 1);
    }

    public static int calculateTavernXPmin(int playerLevel, int guildBonusPercent) {
        double multiplier = 1 + guildBonusPercent / 100.0; // base xp + guild bonus
        return (int) Math.floor((calculateBaseXP(playerLevel) * multiplier) / 11);
    }

    public static int calculateTavernXPmax(int playerLevel, int guildBonusPercent) {
        return calculateTavernXPmin(playerLevel, guildBonusPercent) * 5;
    }

    // --------------- Tavern calculations GOLD ---------------
    private static int calculateBaseGold(int playerLevel) {
        return (int) (Constants.GOLD_CURVE[Math.min(playerLevel, 100)] * 12) / 1000; // Base gold with hard cap at level 100
    }

    public static int calculateTavernGoldMin(int playerLevel, int guildBonusPercent) {
        double multiplier = 1 + guildBonusPercent / 100.0; // base gold + guild bonus
        return (int) Math.floor((calculateBaseGold(playerLevel) * multiplier) / 11);
    }

    public static int calculateTavernGoldMax(int playerLevel, int guildBonusPercent) {
        return calculateTavernGoldMin(playerLevel, guildBonusPercent) * 5;
    }


    // --------------- Experience calculations ---------------

    public static int calculateXPForLevelUp(int level) {
        return Constants.EXPERIENCE_TO_LVLUP[Math.min(level + 1, 100)]; // HARD CAP for level 100
    }

    // --------------- Atribute calculations ---------------

    public static int goldAttributeCost(int attribute) {
        int cost = 0;

        for (int i = 0; i < 5; i++) {

            // Recount atributes to match level (1–100)
            int level = 1 + (attribute + i) / 5;
            level = Math.min(level, 100); // HARD CAP

            int goldValue = Constants.GOLD_CURVE[level];

            // SOFT CAP from level 75
            if (level > 75) {
                double multiplier = 1.0 + (level - 75) * 0.08;
                goldValue = (int) Math.floor(goldValue * multiplier);
            }

            cost += goldValue;
        }

        // Rounding
        cost = (cost / 25) * 5 / 100;

        // Min and max cap
        cost = Math.max(cost, 1);
        cost = Math.min(cost, 100_000); 

        return cost;
    }


    // --------------- Item calculations ---------------

    public static int calculateItemStats(int level) {

        if (level < 5) {
            return 1 + RANDOM.nextInt(4); // level 1-4: -3 to +3, doubled
        }

        if (level < 10) {
            return Math.abs((level + RANDOM.nextInt(7) - 3) * 2); // level 5-9: -3 to +3, doubled
        }

        if (level < 15) {
            return (level + RANDOM.nextInt(9) - 4) * 2;
        }

        return (level + RANDOM.nextInt(11) - 5) * 2; // default value added with +/- 5 randomly, doubled
    }

    public static int calculateItemPrice(int level) {
        //TODO: implement, think about it
        return 1;
    }

}
