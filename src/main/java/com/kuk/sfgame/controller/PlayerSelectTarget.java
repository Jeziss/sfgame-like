package com.kuk.sfgame.controller;

public enum PlayerSelectTarget {
    DETAILS("/player/details"),
    TAVERN("/tavern"),
    SHOP("/weapon-shop"),
    TAVERN_LOCATIONS("/tavern-location"),
    DUNGEONS("/dungeons");


    private final String redirectPath;

    PlayerSelectTarget(String redirectPath) {
        this.redirectPath = redirectPath;
    }

    public String getRedirectPath() {
        return redirectPath;
    }
}
