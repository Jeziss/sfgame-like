package com.kuk.sfgame.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import com.kuk.sfgame.service.impl.PlayerService;
import com.kuk.sfgame.model.Player;
import java.util.List;


@Controller
public class LegacyLeaderboardController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/legacy-leaderboard")
    public String legacyLeaderboard(Model model) {
        List<Player> players = playerService.getPlayersForLeaderboardOrdered();
        model.addAttribute("players", players);
        return "legacy-leaderboard/leaderboard";
    }

     @GetMapping("/legacy-leaderboard/update")
     public String getMethodName() {
         playerService.sortAllPlayersByPower();
         return "redirect:/legacy-leaderboard";
     }
}
