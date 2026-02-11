package com.kuk.sfgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.util.List;

import org.springframework.ui.Model;

import com.kuk.sfgame.dto.QuestDto;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Quest;

import com.kuk.sfgame.service.impl.PlayerService;
import com.kuk.sfgame.service.impl.QuestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class TavernController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private QuestService questService;

    @GetMapping("/tavern")
    public String getTavern(@RequestParam int playerId, Model model) {

        Player player = playerService.getPlayerById(playerId);
        List<QuestDto> quests = playerService.getQuestsForPlayer(playerId);

        model.addAttribute("player", player);
        model.addAttribute("quests", quests);

        return "tavern/tavern-inside";
    }

    @GetMapping("/tavern-location")
    public String getTavernLocation(@RequestParam int playerId, Model model) {
        Player player = playerService.getPlayerWithGearById(playerId);
        Quest quest = questService.getQuestForPlayer(playerId);
        
        model.addAttribute("player", player);
        model.addAttribute("quest", quest);
        return "tavern/tavern-location";
    }

    @PostMapping("tavern/start-quest")
    public String startQuest(@RequestParam int questEnergy,
                             @RequestParam String questLocation,
                             @RequestParam int questXp,
                             @RequestParam int questGold,
                             @RequestParam int playerId) {

        questService.createQuestForPlayer(playerId, questGold, questXp, questEnergy, questLocation);

        
        return "redirect:tavern/tavern-inside?playerId=" + playerId;
    }


    @PostMapping("tavern/fail-quest")
    public String failQuest(@RequestParam int playerId) {
        questService.failQuestForPlayer(playerId);
        return "redirect:tavern/tavern-inside?playerId=" + playerId;
    }
    
    @PostMapping("tavern/finish-quest")
    public String finishQuest(@RequestParam int playerId) {
        questService.finishQuestForPlayer(playerId);
        return "redirect:tavern/tavern-inside?playerId=" + playerId;
    }
    
    
}
