package com.kuk.sfgame.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import com.kuk.sfgame.repository.LegacyLeaderboardRepository;
import com.kuk.sfgame.model.Player;
import java.util.List;

@Controller
public class LegacyLeaderboard {
    @Autowired
    private LegacyLeaderboardRepository legacyLeaderboardRepository;

    @GetMapping("/legacy-leaderboard")
    public String legacyLeaderboard(Model model) {
        List<Player> players = legacyLeaderboardRepository.findAllPlayers();
        model.addAttribute("players", players);
        return "legacy-leaderboard/leaderboard";
    }
}
