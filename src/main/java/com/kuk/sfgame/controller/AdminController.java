package com.kuk.sfgame.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kuk.sfgame.model.Guild;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.repository.GuildRepository;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.service.impl.PlayerService;
import com.kuk.sfgame.service.impl.QuestLocationService;

@Controller
public class AdminController {

    private final PlayerRepository playerRepository;
    private final GuildRepository guildRepository;
    private final QuestLocationService questLocationService;
    private final PlayerService playerService;

    public AdminController(PlayerRepository playerRepository, GuildRepository guildRepository,
                           QuestLocationService questLocationService, PlayerService playerService) {
        this.playerRepository = playerRepository;
        this.guildRepository = guildRepository;
        this.questLocationService = questLocationService;
        this.playerService = playerService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("players", playerRepository.findAllByOrderByNameAsc());
        model.addAttribute("guilds", guildRepository.findAll());
        model.addAttribute("locations", questLocationService.getLocations());
        return "admin/admin-panel";
    }

    @PostMapping("/admin/locations")
    public String updateLocations(
            @RequestParam(value = "activeLocations", required = false) List<String> activeLocations,
            @RequestParam(value = "newLocation", required = false) String newLocation,
            @RequestParam(defaultValue = "save") String action) {

        if ("add".equals(action)) {
            questLocationService.addLocation(newLocation);
        } else if ("delete".equals(action)) {
            questLocationService.removeLocations(activeLocations);
        } else {
            questLocationService.setLocations(activeLocations);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/player")
    public String updatePlayer(
            @RequestParam int playerId,
            @RequestParam(defaultValue = "set") String updateMode,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) Integer xpSet,
            @RequestParam(required = false) Integer goldSet,
            @RequestParam(required = false) Integer xpAdd,
            @RequestParam(required = false) Integer goldAdd,
            @RequestParam(required = false) Integer strength,
            @RequestParam(required = false) Integer constitution,
            @RequestParam(required = false) Integer luck,
            @RequestParam(required = false) Integer energy) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        boolean isAddMode = "add".equals(updateMode);

        if (level != null && !isAddMode) {
            player.setLevel(level);
        }
        Integer xpValue = isAddMode ? xpAdd : xpSet;
        Integer goldValue = isAddMode ? goldAdd : goldSet;

        if (xpValue != null && xpValue != 0) {
            if (isAddMode) {
                player.earnExperience(xpValue);
            } else {
                player.setExperience(xpValue);
            }
        }
        if (goldValue != null && goldValue != 0) {
            if (isAddMode) {
                player.earnGold(goldValue);
            } else {
                player.setGold(goldValue);
            }
        }
        if (strength != null && !isAddMode) {
            player.setStrength(strength);
        }
        if (constitution != null && !isAddMode) {
            player.setConstitution(constitution);
        }
        if (luck != null && !isAddMode) {
            player.setLuck(luck);
        }
        if (energy != null && !isAddMode) {
            player.setEnergy(energy);
        }

        playerRepository.save(player);
        return "redirect:/admin";
    }

    @PostMapping("/admin/guilds")
    public String updateGuilds(
            @RequestParam int guildId,
            @RequestParam int goldBonus,
            @RequestParam int xpBonus,
            @RequestParam int hpBonus,
            @RequestParam int dmgBonus) {

        Guild guild = guildRepository.findById(guildId).orElseThrow(() -> new IllegalArgumentException("Guild not found"));
        guild.setGoldBonusPercent(goldBonus);
        guild.setXpBonusPercent(xpBonus);
        guild.setHpBonusPercent(hpBonus);
        guild.setDmgBonusPercent(dmgBonus);
        guildRepository.save(guild);
        return "redirect:/admin";
    }

    @PostMapping("/admin/reset-energy")
    public String resetAllEnergy() {
        List<Player> players = playerRepository.findAll();
        for (Player player : players) {
            player.setEnergy(100);
        }
        playerRepository.saveAll(players);
        return "redirect:/admin";
    }

    @PostMapping("/admin/refresh-leaderboard")
    public String refreshLegacyLeaderboard() {
        playerService.sortAllPlayersByPower();
        return "redirect:/admin";
    }
}
