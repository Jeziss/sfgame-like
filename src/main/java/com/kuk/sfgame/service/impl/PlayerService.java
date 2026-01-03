package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.PlayerRepository;

import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Equipment;
import com.kuk.sfgame.model.Item;
import com.kuk.sfgame.model.Guild;
import com.kuk.sfgame.model.ItemSlot;
import com.kuk.sfgame.dto.PlayerDto;

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
        player.setGuild(playerGuild.getName());

        return player;
    }



}
