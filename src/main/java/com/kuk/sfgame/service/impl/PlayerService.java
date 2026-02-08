package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.util.Calculation;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.UpgradePricesRecord;
import com.kuk.sfgame.model.Equipment;
import com.kuk.sfgame.model.Item;
import com.kuk.sfgame.model.Guild;
import com.kuk.sfgame.dto.PlayerDto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private GuildService guildService;


    public List<PlayerDto> getPlayerNamesId() {
        List<PlayerDto> players = playerRepository.findAllPlayersNamesId();
        return players;
    }

    public Player getPlayerByName(String playerName) {
        return playerRepository.findPlayerByName(playerName);
    }

    public Player getPlayerById(int id) {
        return playerRepository.findPlayerById(id);
    }

    public List<Player> getPlayersForLeaderboardOrdered(){
        List<Player> players = playerRepository.findAllPlayersWithPositionOrdered();
        return players;
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
            player.setGuild(playerGuild.getName());
        }

        return player;
    }


    public UpgradePricesRecord getUpgradePrices(Player player) {
        int strengthPrice = Calculation.goldAttributeCost(player.getStrength() - 15);    // -15 to reduce the starting 15 points cost
        int constitutionPrice = Calculation.goldAttributeCost(player.getConstitution() - 15);
        int luckPrice = Calculation.goldAttributeCost(player.getLuck());

        return new UpgradePricesRecord(strengthPrice, constitutionPrice, luckPrice);
    }

    public void updatePlayerStats(Player player) {
        playerRepository.updatePlayerStats(player);
    }

    public void updatePlayerGold(int playerId, int newGoldAmount) {
        playerRepository.updatePlayerGold(playerId, newGoldAmount);
    }


    public void sortAllPlayersByPower() {
        List<Player> playersOrdered =  playerRepository.findAllPlayersWithPositionOrdered();
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
            playerRepository.updatePlayerPosition(sorted.get(i).getId(), i + 1);
        }

    }
}
