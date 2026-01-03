package com.kuk.sfgame.dto;

public class PlayerChoiceDto {
    private String selectedPlayerName; // or Long/Integer if IDs are numeric
    private int selectedPlayerId;
    // getter & setter
    public String getSelectedPlayerName() { return selectedPlayerName; }
    public void setSelectedPlayerName(String selectedPlayerName) { this.selectedPlayerName = selectedPlayerName; }

    public int getSelectedPlayerId() { return selectedPlayerId; }
    public void setSelectedPlayerId(int selectedPlayerId) { this.selectedPlayerId = selectedPlayerId; }

}
