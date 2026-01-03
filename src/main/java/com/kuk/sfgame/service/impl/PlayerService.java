package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.PlayerRepository;

import com.kuk.sfgame.model.Player;

import com.kuk.sfgame.dto.PlayerDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;


    public List<PlayerDto> getPlayerNamesId() {
        List<PlayerDto> players = playerRepository.findAllPlayersNamesId();
        return players;
    }

    public Player getPlayerByName(String playerName) {
        return playerRepository.findPlayerByName(playerName);
    }

    public Player getPlayerById(int id) {
        return playerRepository.findPlayerById(id);
    }

    public List<Player> getPlayersForLeaderboardOrdered(){
        List<Player> players = playerRepository.findAllPlayersWithPositionOrdered();
        return players;
    }


}
