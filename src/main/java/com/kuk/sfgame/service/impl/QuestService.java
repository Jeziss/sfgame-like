package com.kuk.sfgame.service.impl;

import org.springframework.stereotype.Service;

import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Quest;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.repository.QuestRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;

    private final PlayerRepository playerRepository;

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

        Player player = playerRepository.findPlayerById(playerId);

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

    //TODO
    public void completeQuest(int playerId) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("No active quest for player with id: " + playerId));

        Player player = quest.getPlayer();

        player.earnExperience(quest.getXpReward());
        player.earnGold(quest.getGoldReward());

        // Odečíst energii
        int newEnergy = player.getEnergy() - quest.getEnergyCost();
        if (newEnergy < 0) {
            newEnergy = 0; // Energie nesmí být záporná
        }
        player.setEnergy(newEnergy);

        playerRepository.save(player);
        questRepository.delete(quest);
    }

    //TODO
    public void failQuestForPlayer(int playerId) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("No active quest for player with id: " + playerId));

        Player player = quest.getPlayer();

        // Odečíst energii
        int newEnergy = player.getEnergy() - quest.getEnergyCost();
        if (newEnergy < 0) {
            newEnergy = 0; // Energie nesmí být záporná
        }
        player.setEnergy(newEnergy);

        playerRepository.save(player);
        questRepository.delete(quest);
    }
}

