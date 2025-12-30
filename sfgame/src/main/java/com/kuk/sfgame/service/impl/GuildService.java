package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.GuildRepository;

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

    public Tuple6<List<Player>, List<Player>, String, String, GuildBonus, GuildBonus> getPlayersSplitToGuild(){
        List<Player> guild1Players = guildRepository.findPlayersByGuildId(1);
        List<Player> guild2Players = guildRepository.findPlayersByGuildId(2);
        
        Guild guild1 = guildRepository.findGuildById(1);
        Guild guild2 = guildRepository.findGuildById(2);

        String guildName1 = (guild1 != null) ? guild1.getName() : "Guild 1";
        String guildName2 = (guild2 != null) ? guild2.getName() : "Guild 2";

        GuildBonus guildBonus1 = (guild1 != null) ? guild1.getGuildBonus() : new GuildBonus(0, 0, 0, 0);
        GuildBonus guildBonus2 = (guild2 != null) ? guild2.getGuildBonus() : new GuildBonus(0, 0, 0, 0);

        return Tuple.of(guild1Players, guild2Players, guildName1, guildName2, guildBonus1, guildBonus2);
    }
}
