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
    
    private Constants() {
        // Utility class - prevent instantiation
    }
}

