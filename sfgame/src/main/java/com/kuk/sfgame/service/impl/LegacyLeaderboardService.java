package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.LegacyLeaderboardRepository;
import com.kuk.sfgame.model.Player;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class LegacyLeaderboardService {
    
    @Autowired
    private LegacyLeaderboardRepository repository;

    public List<Player> getAllPlayers(){
        return repository.findAllPlayers();
    }
}
