package com.kuk.sfgame.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.kuk.sfgame.dto.QuestDto;
import com.kuk.sfgame.model.GuildBonus;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Quest;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.repository.QuestRepository;
import com.kuk.sfgame.util.Calculation;
import com.kuk.sfgame.util.Constants;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;

    private final PlayerRepository playerRepository;

    private final PlayerService playerService;
    
    private final GuildService guildService;

    public List<QuestDto> getQuestsForPlayer(int playerId) {

        Player player = playerService.getPlayerById(playerId); // just to check if player exists, otherwise throw exception
        
        GuildBonus guildBonus = guildService.getGuildBonusForPlayer(playerId);

        int maxGold = Calculation.calculateTavernGoldMax(player.getLevel(),guildBonus.gold);
        int minGold  = Calculation.calculateTavernGoldMin(player.getLevel(),guildBonus.gold);
        int maxXp = Calculation.calculateTavernXPmax(player.getLevel(),guildBonus.gold);
        int minXp = Calculation.calculateTavernXPmin(player.getLevel(),guildBonus.gold);

        int numberOfQuests = guildBonus.questOfferNumber;; 

        List<QuestDto> quests = new ArrayList<>();

        for (int i = 0; i < numberOfQuests; i++) {
            double proportionalityConstant = ThreadLocalRandom.current().nextDouble(0, 1);
            int goldReward = (int) (minGold + proportionalityConstant * (maxGold - minGold));
            int xpReward = (int) (minXp + (1 - proportionalityConstant) * (maxXp - minXp));
            
            int multiplier = ThreadLocalRandom.current().nextInt(1, 5);
            int energyCost = multiplier * 5;

            goldReward = (int) (goldReward * multiplier);
            xpReward = (int) (xpReward * multiplier);

            String location = Constants.LOCATION_NAMES.get(ThreadLocalRandom.current().nextInt(Constants.LOCATION_NAMES.size()));

            quests.add(new QuestDto(xpReward, goldReward, energyCost, location));
        }
        return quests;
    }


    public Quest getQuestForPlayer(int playerId) {
        return questRepository.findByPlayerId(playerId).orElse(null);
    }

     public Quest createQuestForPlayer(
            int playerId,
            int gold,
            int xp,
            int energy,
            String location
    ) {

        Player player = playerRepository.findById(playerId).orElse(null);

        if (player == null) {
            throw new IllegalArgumentException("Player not found with id: " + playerId);
        }

        // ochrana – hráč už má quest
        if (questRepository.findByPlayerId(playerId).isPresent()) {
            throw new RuntimeException("Player already has active quest");
        }

        Quest quest = new Quest();
        quest.setPlayer(player);
        quest.setGoldReward(gold);
        quest.setXpReward(xp);
        quest.setEnergyCost(energy);
        quest.setLocation(location);

        return questRepository.save(quest);
    }

    public void completeQuestForPlayer(int playerId) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("No active quest for player with id: " + playerId));

        Player player = quest.getPlayer();
        player.setQuest(null);


        player.earnExperience(quest.getXpReward());
        player.earnGold(quest.getGoldReward());

        playerRepository.save(player);
        questRepository.delete(quest);
    }

    public void failQuestForPlayer(int playerId) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("No active quest for player with id: " + playerId));

        Player player = quest.getPlayer();
        player.setQuest(null);

        playerRepository.save(player);
        questRepository.delete(quest);
    }
}

