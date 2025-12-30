package com.kuk.sfgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

import org.springframework.ui.Model;

import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.GuildBonus;

import com.kuk.sfgame.service.impl.GuildService;

import io.vavr.Tuple6;

@Controller
public class GuildController {

    @Autowired
    private GuildService guildService;

    @GetMapping("/guilds")
    public String getGuilds(Model model) {
        Tuple6<List<Player>,List<Player>,String,String,GuildBonus,GuildBonus> splitGuildPlayers = guildService.getPlayersSplitToGuild();

        model.addAttribute("guild1players", splitGuildPlayers._1());
        model.addAttribute("guild2players", splitGuildPlayers._2());

        model.addAttribute("guild1Name", splitGuildPlayers._3());
        model.addAttribute("guild2Name", splitGuildPlayers._4());

        model.addAttribute("guild1Bonus", splitGuildPlayers._5());
        model.addAttribute("guild2Bonus", splitGuildPlayers._6());

        return "guilds/guilds-H2H";
    }
}
