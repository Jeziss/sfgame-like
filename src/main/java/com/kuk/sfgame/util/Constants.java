package com.kuk.sfgame.util;

/**
 * Application-wide constants
 */
public class Constants {
    
    // API endpoints
    public static final String API_BASE_PATH = "/api";
    public static final String API_V1 = API_BASE_PATH + "/v1";
    
    // Date formats
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final int[] EXPERIENCE_TO_LVLUP = {
        0, 400, 900, 1400, 1800, 2200, 2890, 3580, 4405, 5355, 6435, 7515, 8925,
        10335, 11975, 13715, 15730, 17745, 20250, 22755, 25620, 28660, 32060, 35460, 39535,
        43610, 48155, 52935, 58260, 63585, 69760, 75935, 82785, 89905, 97695, 105485, 114465,
        123445, 133260, 143425, 154545, 165665, 178210, 190755, 204430, 218540, 233785, 249030,
        266140, 283250, 301715, 320685, 341170, 361655, 384360, 407065, 431545, 456650, 483530,
        510410, 540065, 569720, 601435, 633910, 668670, 703430, 741410, 779390, 819970, 861400,
        905425, 949450, 997485, 1045520, 1096550, 1148600, 1203920, 1259240, 1319085, 1378930,
        1442480, 1507225, 1575675, 1644125, 1718090, 1792055, 1870205, 1949685, 2033720, 2117755,
        2208040, 2298325, 2393690, 2490600, 2592590, 2694580, 2803985, 2913390, 3028500, 3145390
    };

    public static final int[] GOLD_CURVE = buildGoldCurve();



    public static int[] buildGoldCurve() {
        int MAX_LEVEL = 100;
        int[] gold = new int[MAX_LEVEL + 1];

        // základní hodnoty
        gold[0] = 0;
        gold[1] = 25;
        gold[2] = 50;
        gold[3] = 75;

        for (int level = 4; level <= MAX_LEVEL; level++) {

            int prev = gold[level - 1];
            int half = gold[level / 2] / 3;
            int third = gold[level / 3] / 4;

            int value = (prev + half + third);
            value = (value / 5) * 5; // zaokrouhlení na 5

            gold[level] = value;
        }

        return gold;
    }
    
    private Constants() {
        // Utility class - prevent instantiation
    }
}

