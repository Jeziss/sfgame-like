package com.kuk.sfgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.util.List;

import org.springframework.ui.Model;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.kuk.sfgame.service.impl.PlayerService;
import com.kuk.sfgame.dto.PlayerChoiceDto;
import com.kuk.sfgame.dto.PlayerDto;
import com.kuk.sfgame.model.Player;


@Controller
public class PlayerController {
    
    @Autowired
    private PlayerService playerService;

    @GetMapping("/player")
    public String getPlayerChoice(Model model, @RequestParam PlayerSelectTarget target) {
        List<PlayerDto> players = playerService.getPlayerNamesId();
        if (players == null || players.isEmpty()) {
            model.addAttribute("errorMessage", "No players found");
            return "player/player-choice";
        }
        model.addAttribute("players", players);

        model.addAttribute("playerChoiceDto", new PlayerChoiceDto());
        model.addAttribute("target", target);
        
        return "player/player-choice";
    }

    @PostMapping("/player/choice")
    public String postPlayerChoice(@RequestParam("selectedPlayerId") int id,
                                    @RequestParam PlayerSelectTarget target, 
                                    RedirectAttributes redirectAttributes) {
        // Handle the player choice submission
        // For example, redirect to the player's detail page
        Player player = playerService.getPlayerById(id);
        
        
        if (player == null) {
            // Přidej zprávu do redirectu, aby Thymeleaf mohl zobrazit error
            redirectAttributes.addFlashAttribute("errorMessage", "Player not found: " + id);
            return "redirect:/player"; // zpět na výběr hráče
        }

        return "redirect:" + target.getRedirectPath() + "?playerId=" + id; // přesměrování na cílovou stránku s ID hráče jako param
    }

    @GetMapping("/player/details")
    public String getPlayerDetails(@RequestParam("playerId") int id, Model model) {
        Player player = playerService.getPlayerWithGearById(id);

        if (player == null) {
            model.addAttribute("errorMessage", "Player not found");
            return "player/player-choice"; // nebo stránka s chybou
        }

        if (player.getEquipment() == null) {
            model.addAttribute("errorMessage", "Player equipment not found");
            return "player/player-choice";
        }

        model.addAttribute("player", player);
        return "player/player-details"; // Thymeleaf šablona s detaily hráče
    }

}
