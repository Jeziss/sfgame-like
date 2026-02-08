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
import com.kuk.sfgame.model.UpgradePricesRecord;




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
        model.addAttribute("upgradePrices", playerService.getUpgradePrices(player));

        return "player/player-details"; // Thymeleaf šablona s detaily hráče
    }

    @PostMapping("player/increaseStat")
    public String increaseStat(@RequestParam("playerId") int id,
                                @RequestParam("stat") String stat,
                                RedirectAttributes redirectAttributes) {
        
        Player player = playerService.getPlayerById(id);
        if (player == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Player not found: " + id);
            return "redirect:/player";
        }

        UpgradePricesRecord prices;
        int cost;
        try {
            prices = playerService.getUpgradePrices(player);
            switch (stat.toLowerCase()) {
                case "strength":
                    cost = prices.strength();
                    if (player.getGold() < cost) {
                        throw new IllegalArgumentException("Not enough gold to upgrade strength.");
                    }
                    player.setStrength(player.getStrength() + 1);
                    break;
                case "constitution":
                    cost = prices.constitution();
                    if (player.getGold() < cost) {
                        throw new IllegalArgumentException("Not enough gold to upgrade constitution.");
                    }
                    player.setConstitution(player.getConstitution() + 1);
                    break;
                case "luck":
                    cost = prices.luck();
                    if (player.getGold() < cost) {
                        throw new IllegalArgumentException("Not enough gold to upgrade luck.");
                    }
                    player.setLuck(player.getLuck() + 1);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid stat: " + stat);
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/player/details?playerId=" + id;
        }

        player.setGold(player.getGold() - cost);
        playerService.updatePlayerGold(id, player.getGold());
        playerService.updatePlayerStats(player);
        redirectAttributes.addFlashAttribute("successMessage", "Increased " + stat + " for player " + player.getName() + " at a cost of " + cost + " gold.");
        
        return "redirect:/player/details?playerId=" + id; // přesměrování zpět na detail hráče
    }
    

}
