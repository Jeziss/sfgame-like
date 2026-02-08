package com.kuk.sfgame.controller;

public enum PlayerSelectTarget {
    DETAILS("/player/details"),
    TAVERN("/tavern"),
    SHOP("/weapon-shop");

    private final String redirectPath;

    PlayerSelectTarget(String redirectPath) {
        this.redirectPath = redirectPath;
    }

    public String getRedirectPath() {
        return redirectPath;
    }
}
