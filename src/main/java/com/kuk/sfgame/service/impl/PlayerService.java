package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.LegacyLeaderboardRepository;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.util.Calculation;


import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.UpgradePricesRecord;
import com.kuk.sfgame.model.Equipment;
import com.kuk.sfgame.model.Item;
import com.kuk.sfgame.model.LegacyLeaderboard;
import com.kuk.sfgame.model.Guild;
import com.kuk.sfgame.model.GuildBonus;
import com.kuk.sfgame.dto.PlayerDto;

import java.util.ArrayList;
import java.util.Collections;
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
            Player player = getPlayerById(lb.getPlayerId());
            if (player != null) {
                player.setPosition(lb.getPosition());
                players.add(player);
            }
        }

        return players;
    }

    @Transactional
    public void updatePlayerPosition(int playerId, int newPosition) {
        LegacyLeaderboard leaderboard = leaderboardRepository.findByPlayerId(playerId);
        if (leaderboard == null) {
            throw new IllegalArgumentException("Player not found in leaderboard with id: " + playerId);
        }

        leaderboard.setPosition(newPosition);
        leaderboardRepository.save(leaderboard);
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

    @Transactional
    public void sortAllPlayersByPower() {
        List<Player> playersOrdered = getPlayersForLeaderboardOrdered();
        playersOrdered = playersOrdered.stream()
                .map(player -> {
                    Player fullPlayer = getPlayerWithGearById(player.getId());
                    fullPlayer.setPosition(player.getPosition());
                    return fullPlayer;
                })
        .toList();

        // Fighting from the weakest to the strongest. The strongesest player does not have to fight everyone.
        List<Player> reversedPlayers = new ArrayList<>(playersOrdered);
        Collections.reverse(reversedPlayers);

        // Players will be sorted using insert sort. An transitive property is assumed.
        List<Player> sorted = new ArrayList<>();

        for (Player p : reversedPlayers) {
            boolean inserted = false;
            for (int i = 0; i < sorted.size(); i++) {
                Player current = sorted.get(i);
                if (fight(p, current)) { // p porazí current 
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
            updatePlayerPosition(sorted.get(i).getId(), i + 1);
        }
    }

    public boolean fight(Player player1, Player player2) {

        GuildBonus guildBonus1 = guildService.getGuildBonusForPlayer(player1.getId());
        GuildBonus guildBonus2 = guildService.getGuildBonusForPlayer(player2.getId());

        int HP1 = Calculation.calculatePlayerHP(player1, guildBonus1);
        int HP2 = Calculation.calculatePlayerHP(player2, guildBonus2);

        int minDamage1 = Calculation.calculateMinPlayerDamage(player1, guildBonus1);
        int minDamage2 = Calculation.calculateMinPlayerDamage(player2, guildBonus2);

        int maxDamage1 = Calculation.calculateMaxPlayerDamage(player1, guildBonus1);
        int maxDamage2 = Calculation.calculateMaxPlayerDamage(player2, guildBonus2);

        double criticalChance1 = 1;
        double criticalChance2 = 1;

        boolean player1Turn;

        if (player1.getLuck() > player2.getLuck()) {
            player1Turn = true;
        } else if (player2.getLuck() > player1.getLuck()) {
            player1Turn = false;
        } else {
            // Coinflip if luck is the same
            player1Turn = ThreadLocalRandom.current().nextBoolean();
        }

        int damageTo1, damageTo2;

        while (HP1 > 0 && HP2 > 0) {
            boolean criticalHit = false;
            if (player1Turn) {
                damageTo2 = ThreadLocalRandom.current().nextInt(minDamage1, maxDamage1 + 1);
                criticalHit = ThreadLocalRandom.current().nextDouble() < criticalChance1;

                if (criticalHit) {
                    damageTo2 = Calculation.calculateCritDamage(damageTo2);
                }
                // Deal damagae to player 2
                HP2 -= damageTo2;
                
                // Check if player 2 is defeated
                if (HP2 <= 0) {
                    return true; // player1 wins
                }

                player1Turn = false;
            } else {
                // Player 2 attacks
                damageTo1 = ThreadLocalRandom.current().nextInt(minDamage2, maxDamage2 + 1);
                criticalHit = ThreadLocalRandom.current().nextDouble() < criticalChance2;

                if (criticalHit) {
                    damageTo1 = Calculation.calculateCritDamage(damageTo1);
                }

                HP1 -= damageTo1;

                if (HP1 <= 0) {
                    return false; // player2 wins
                }
                player1Turn = true;
            }
        }

        if (HP1 > 0) {
            return true; // player1 wins
        } else {
            return false; // player2 wins
        }
    }


    //TODO: check, coded offline!
    public void resetTheDay() {
        playerRepository.updateAllPlayersEnergy(100);
    }



}
