package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.GuildRepository;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Guild;
import com.kuk.sfgame.model.GuildBonus;

import java.util.List;

import io.vavr.Tuple6;
import io.vavr.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildService {
    
    @Autowired
    private GuildRepository guildRepository;
    
    @Autowired
    private PlayerRepository playerRepository;

    public Tuple6<List<Player>, List<Player>, String, String, GuildBonus, GuildBonus> getPlayersSplitToGuild(){
        List<Player> allPlayers = playerRepository.findAllWithGuild();

        List<Player> guild1Players = allPlayers.stream()
            .filter(p -> p.getGuild() != null && p.getGuild().getId() == 1)
            .toList();

        List<Player> guild2Players = allPlayers.stream()
            .filter(p -> p.getGuild() != null && p.getGuild().getId() == 2)
            .toList();
        
        Guild guild1 = guild1Players.isEmpty() ? null : guild1Players.get(0).getGuild();
        Guild guild2 = guild2Players.isEmpty() ? null : guild2Players.get(0).getGuild();

        String guildName1 = (guild1 != null) ? guild1.getName() : "Guild 1";
        String guildName2 = (guild2 != null) ? guild2.getName() : "Guild 2";

        GuildBonus guildBonus1 = (guild1 != null) ? guild1.getGuildBonus() : new GuildBonus(0, 0, 0, 0, 0);
        GuildBonus guildBonus2 = (guild2 != null) ? guild2.getGuildBonus() : new GuildBonus(0, 0, 0, 0, 0);

        return Tuple.of(guild1Players, guild2Players, guildName1, guildName2, guildBonus1, guildBonus2);
    }

    public Guild getGuildByPlayerId(int playerId) {
        return guildRepository.findGuildByPlayerId(playerId);
    }

    public GuildBonus getGuildBonusForPlayer(int playerId) {
        Guild guild = getGuildByPlayerId(playerId);
        return (guild != null) ? guild.getGuildBonus() : new GuildBonus(0, 0, 0, 0, 0);
    }
}
