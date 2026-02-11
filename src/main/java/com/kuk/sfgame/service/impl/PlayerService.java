package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.LegacyLeaderboardRepository;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.util.Calculation;
import com.kuk.sfgame.util.Constants;

import io.vavr.collection.List.Cons;

import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Quest;
import com.kuk.sfgame.model.UpgradePricesRecord;
import com.kuk.sfgame.model.Equipment;
import com.kuk.sfgame.model.Item;
import com.kuk.sfgame.model.LegacyLeaderboard;
import com.kuk.sfgame.model.Guild;
import com.kuk.sfgame.model.GuildBonus;
import com.kuk.sfgame.dto.PlayerDto;
import com.kuk.sfgame.dto.QuestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private GuildService guildService;

    @Autowired    
    private LegacyLeaderboardRepository leaderboardRepository;


    public List<PlayerDto> getPlayerNamesId() {
        List<PlayerDto> players = playerRepository.findAllPlayersNamesId();
        return players;
    }

    public Player getPlayerByName(String playerName) {
        return playerRepository.findByName(playerName).orElse(null);
    }

    public Player getPlayerById(int id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Player> getPlayersForLeaderboardOrdered(){
        List<LegacyLeaderboard> leaderboard = leaderboardRepository.findAllByOrderByPositionAsc();

        List<Player> players = new ArrayList<>();
        for (LegacyLeaderboard lb : leaderboard) {
            players.add(getPlayerById(lb.getPlayerId()));
        }

        return players;
    }

    @Transactional
    public void updatePlayerPosition(int playerId, int newPosition) {
        leaderboardRepository.updatePosition(playerId, newPosition);
    }


    @Transactional
    public Player getPlayerWithGearById(int id) {
        Player player = getPlayerById(id);
        if (player == null) {
            return null;
        }

        List<Item> items = itemService.getItemsForPlayer(id);
        Equipment equip = new Equipment();

        for (Item item : items) {
            if (item != null && item.getEquippedSlot() != null) {
                equip.equip(item);
            }
        }

        player.setEquipment(equip);

        Guild playerGuild = guildService.getGuildByPlayerId(id);
        if (playerGuild != null) {
            player.setGuild(playerGuild);
        }

        return player;
    }


    public UpgradePricesRecord getUpgradePrices(Player player) {
        int strengthPrice = Calculation.goldAttributeCost(player.getStrength() - 15);    // -15 to reduce the starting 15 points cost
        int constitutionPrice = Calculation.goldAttributeCost(player.getConstitution() - 15);
        int luckPrice = Calculation.goldAttributeCost(player.getLuck());

        return new UpgradePricesRecord(strengthPrice, constitutionPrice, luckPrice);
    }

    public void save(Player player) {
        playerRepository.save(player);
    }


    public void sortAllPlayersByPower() {
        List<Player> playersOrdered = getPlayersForLeaderboardOrdered();
        playersOrdered.stream()
                .map(player -> {
                    Player fullPlayer = getPlayerWithGearById(player.getId());
                    fullPlayer.setPosition(player.getPosition());
                    return fullPlayer;
                })
        .toList();

        // Players will be sorted using insert sort. An transitive property is assumed.
        
        List<Player> sorted = new ArrayList<>();

        for (Player p : playersOrdered) {
            boolean inserted = false;
            for (int i = 0; i < sorted.size(); i++) {
                Player current = sorted.get(i);
                if (p.getLevel() > current.getLevel() ) { // p porazí current //TODO: implement fight method
                    sorted.add(i, p); // vlož p před current
                    inserted = true;
                    break;
                }
            }
            if (!inserted) {
                sorted.add(p); // prohra se všemi → na konec
            }
        }


        for (int i = 0; i < sorted.size(); i++) {
            playerRepository.updatePlayerPosition(sorted.get(i), i);
        }
    }


    public List<QuestDto> getQuestsForPlayer(int playerId) {

        Player player = getPlayerById(playerId); // just to check if player exists, otherwise throw exception
        
        GuildBonus guildBonus = guildService.getGuildBonusForPlayer(playerId);

        int maxGold = Calculation.calculateTavernGoldMax(player.getLevel(),guildBonus.gold);
        int minGold  = Calculation.calculateTavernGoldMin(player.getLevel(),guildBonus.gold);
        int maxXp = Calculation.calculateTavernXPmax(player.getLevel(),guildBonus.gold);
        int minXp = Calculation.calculateTavernXPmin(player.getLevel(),guildBonus.gold);

        // TODO: Implement ability toh have bonus quests
        int numberOfQuests = 3; 

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
}
