package com.kuk.sfgame.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.kuk.sfgame.dto.QuestDto;
import com.kuk.sfgame.model.GuildBonus;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Quest;
import com.kuk.sfgame.model.QuestHistory;
import com.kuk.sfgame.dto.QuestHistorySummaryDto;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.repository.QuestHistoryRepository;
import com.kuk.sfgame.repository.QuestRepository;
import com.kuk.sfgame.util.Calculation;
import com.kuk.sfgame.util.Constants;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;

    private final QuestHistoryRepository questHistoryRepository;

    private final PlayerRepository playerRepository;

    private final PlayerService playerService;
    
    private final GuildService guildService;
    private final QuestLocationService questLocationService;

    public List<QuestDto> getQuestsForPlayer(int playerId) {

        Player player = playerService.getPlayerById(playerId); // just to check if player exists, otherwise throw exception

        if (player == null) {
            throw new IllegalArgumentException("Player not found with id: " + playerId);
        }

        // Ensure energy initialized
        if (player.getEnergy() == null) {
            player.setEnergy(100);
            playerRepository.save(player);
        }

        // Do not generate quests when player has no energy or already has an active quest
        if (player.getEnergy() < 5 || questRepository.findByPlayerId(playerId).isPresent()) {
            return new ArrayList<>();
        }

        GuildBonus guildBonus = guildService.getGuildBonusForPlayer(playerId);

        int maxGold = Calculation.calculateTavernGoldMax(player.getLevel(),guildBonus.gold);
        int minGold  = Calculation.calculateTavernGoldMin(player.getLevel(),guildBonus.gold);
        int maxXp = Calculation.calculateTavernXPmax(player.getLevel(),guildBonus.xp);
        int minXp = Calculation.calculateTavernXPmin(player.getLevel(),guildBonus.xp);

        //TODO: implement number of quests based on guild bonus, for now it's just 3 for everyone
        //int numberOfQuests = guildBonus.questOfferNumber;; 
        int numberOfQuests = 3; // prozatím pevně, dokud nebude implementace guild bonusů hotová
        List<QuestDto> quests = new ArrayList<>();

        int maxEnergyMultiplier = Math.min(4, player.getEnergy() / 5);
        if (maxEnergyMultiplier < 1) {
            return new ArrayList<>();
        }

        for (int i = 0; i < numberOfQuests; i++) {
            double proportionalityConstant = ThreadLocalRandom.current().nextDouble(0, 1);
            int goldReward = (int) (minGold + proportionalityConstant * (maxGold - minGold));
            int xpReward = (int) (minXp + (1 - proportionalityConstant) * (maxXp - minXp));
            
            int multiplier = ThreadLocalRandom.current().nextInt(1, maxEnergyMultiplier + 1);
            int energyCost = multiplier * 5;

            goldReward = (int) (goldReward * multiplier);
            xpReward = (int) (xpReward * multiplier);

            String location = questLocationService.getRandomLocation();

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

        // Initialize energy for players that don't have it set (default max 100)
        if (player.getEnergy() == null) {
            player.setEnergy(100);
            playerRepository.save(player);
        }

        // ochrana – hráč už má quest
        if (questRepository.findByPlayerId(playerId).isPresent()) {
            throw new RuntimeException("Player already has active quest");
        }
        // Check if player has enough energy for the quest
        if (player.getEnergy() < energy) {
            throw new IllegalArgumentException("Not enough energy");
        }

        // Deduct energy and persist before creating the quest
        player.setEnergy(player.getEnergy() - energy);
        playerRepository.save(player);

        Quest quest = new Quest();
        quest.setPlayer(player);
        quest.setGoldReward(gold);
        quest.setXpReward(xp);
        quest.setEnergyCost(energy);
        quest.setLocation(location);

        return questRepository.save(quest);
    }

    public int completeQuestForPlayer(int playerId) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("No active quest for player with id: " + playerId));

        Player player = quest.getPlayer();
        player.setQuest(null);

        int levelAtCompletion = player.getLevel();
        boolean leveledUp = player.earnExperience(quest.getXpReward());
        player.earnGold(quest.getGoldReward());

        QuestHistory history = new QuestHistory();
        history.setPlayer(player);
        history.setPlayerLevel(levelAtCompletion);
        history.setLocation(quest.getLocation());
        history.setEnergyCost(quest.getEnergyCost());
        history.setXpReward(quest.getXpReward());
        history.setGoldReward(quest.getGoldReward());
        history.setSuccess(true);
        history.setCompletedAt(LocalDateTime.now());
        questHistoryRepository.save(history);

        playerRepository.save(player);
        questRepository.delete(quest);

        return leveledUp ? player.getLevel() : 0;
    }

    public void failQuestForPlayer(int playerId) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("No active quest for player with id: " + playerId));

        Player player = quest.getPlayer();
        player.setQuest(null);

        int levelAtCompletion = player.getLevel();
        QuestHistory history = new QuestHistory();
        history.setPlayer(player);
        history.setPlayerLevel(levelAtCompletion);
        history.setLocation(quest.getLocation());
        history.setEnergyCost(quest.getEnergyCost());
        history.setXpReward(quest.getXpReward());
        history.setGoldReward(quest.getGoldReward());
        history.setSuccess(false);
        history.setCompletedAt(LocalDateTime.now());
        questHistoryRepository.save(history);

        playerRepository.save(player);
        questRepository.delete(quest);
    }

    public void failAllActiveQuests() {
        List<Quest> activeQuests = questRepository.findAll();
        for (Quest quest : activeQuests) {
            if (quest.getPlayer() == null) {
                continue;
            }
            Player player = quest.getPlayer();
            player.setQuest(null);

            QuestHistory history = new QuestHistory();
            history.setPlayer(player);
            history.setPlayerLevel(player.getLevel());
            history.setLocation(quest.getLocation());
            history.setEnergyCost(quest.getEnergyCost());
            history.setXpReward(quest.getXpReward());
            history.setGoldReward(quest.getGoldReward());
            history.setSuccess(false);
            history.setCompletedAt(LocalDateTime.now());
            questHistoryRepository.save(history);

            playerRepository.save(player);
            questRepository.delete(quest);
        }
    }

    public List<QuestHistory> getQuestHistory(String playerName, String success, LocalDate date) {
        boolean filterByPlayer = playerName != null && !playerName.isBlank();
        boolean filterBySuccess = success != null && !"ALL".equalsIgnoreCase(success);
        boolean filterByDate = date != null;

        List<QuestHistory> history = questHistoryRepository.findAllByOrderByCompletedAtDesc();

        if (filterByPlayer) {
            history = history.stream()
                    .filter(item -> item.getPlayer().getName().toLowerCase().contains(playerName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (filterBySuccess) {
            boolean isSuccess = "SUCCESS".equalsIgnoreCase(success);
            history = history.stream()
                    .filter(item -> item.isSuccess() == isSuccess)
                    .collect(Collectors.toList());
        }

        if (filterByDate) {
            history = history.stream()
                    .filter(item -> item.getCompletedAt().toLocalDate().equals(date))
                    .collect(Collectors.toList());
        }

        return history;
    }

    public List<QuestHistorySummaryDto> getQuestHistorySummary(LocalDate date) {
        Map<LocalDate, List<QuestHistory>> grouped = questHistoryRepository.findAllByOrderByCompletedAtDesc()
                .stream()
                .filter(item -> date == null || item.getCompletedAt().toLocalDate().equals(date))
                .collect(Collectors.groupingBy(item -> item.getCompletedAt().toLocalDate()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    LocalDate day = entry.getKey();
                    long totalEnergy = entry.getValue().stream().mapToLong(QuestHistory::getEnergyCost).sum();
                    long totalGold = entry.getValue().stream().mapToLong(QuestHistory::getGoldReward).sum();
                        long totalXp = entry.getValue().stream().mapToLong(QuestHistory::getXpReward).sum();
                        BigDecimal rewardPerEnergy = totalEnergy == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(totalGold)
                            .divide(BigDecimal.valueOf(totalEnergy), 2, RoundingMode.HALF_UP);
                        BigDecimal xpPerEnergy = totalEnergy == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(totalXp)
                            .divide(BigDecimal.valueOf(totalEnergy), 2, RoundingMode.HALF_UP);
                        return new QuestHistorySummaryDto(day, totalEnergy, totalGold, totalXp, rewardPerEnergy, xpPerEnergy);
                })
                .sorted((a, b) -> b.day().compareTo(a.day()))
                .collect(Collectors.toList());
    }
}

