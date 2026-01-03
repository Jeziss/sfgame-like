package com.kuk.sfgame.dto;

public class PlayerDto {
    
    private int id;
    private String name;
    private int level;
    private String guildName;

    // Constructors, getters, and setters
    public PlayerDto() { }

    public PlayerDto(int id, String name, int level, String guildName) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.guildName = guildName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }
}
