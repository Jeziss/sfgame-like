package com.kuk.sfgame.service.impl;

import com.kuk.sfgame.repository.GuildRepository;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Guild;
import com.kuk.sfgame.model.GuildBonus;

import java.util.List;
import java.util.Collections;

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
        List<Player> guild2Players = playerRepository.findByGuildId(2);
        List<Player> guild1Players = playerRepository.findByGuildId(1);
        
        if (guild1Players.isEmpty() || guild2Players.isEmpty()) {
            throw new IllegalStateException("No players found in any guild");
        }    

        Guild guild1 = guildRepository.findById(1);
        Guild guild2 = guildRepository.findById(2);

        String guildName1 = (guild1 != null) ? guild1.getName() : "Guild 1";
        String guildName2 = (guild2 != null) ? guild2.getName() : "Guild 2";

        GuildBonus guildBonus1 = (guild1 != null) ? guild1.getGuildBonus() : new GuildBonus(0, 0, 0, 0, 0);
        GuildBonus guildBonus2 = (guild2 != null) ? guild2.getGuildBonus() : new GuildBonus(0, 0, 0, 0, 0);

        return Tuple.of(guild1Players, guild2Players, guildName1, guildName2, guildBonus1, guildBonus2);
    }

    public Guild getGuildByPlayerId(int playerId) {
        return guildRepository.findByPlayerId(playerId);
    }

    public GuildBonus getGuildBonusForPlayer(int playerId) {
        Guild guild = getGuildByPlayerId(playerId);
        return (guild != null) ? guild.getGuildBonus() : new GuildBonus(0, 0, 0, 0, 0);
    }

    //TODO: Check, coded offline
    public void updateGuildBonuses(int id, GuildBonus newBonus) {
        guildRepository.updateGuildBonuses(id, newBonus.gold, newBonus.xp, newBonus.hp, newBonus.dmg, newBonus.questOfferNumber);

        updateGuildBonusGold(id, newBonus.gold);
        updateGuildBonusXP(id, newBonus.xp);

        updateGuildBonusDamage(id, newBonus.dmg);
        updateGuildBonusHP(id, newBonus.hp);

        updateGuildBonusTavernQuests(id, newBonus.questOfferNumber);
    }

    public void updateGuildBonusGold(int id, int newGoldBonus) {
        guildRepository.updateGoldBonus(id, newGoldBonus);
    }

    public void updateGuildBonusXP(int id, int newXPBonus) {
        guildRepository.updateXpBonus(id, newXPBonus);
    }

    public void updateGuildBonusHP(int id, int newHPBonus) {
        guildRepository.updateHpBonus(id, newHPBonus);
    }

    public void updateGuildBonusDamage(int id, int newDamageBonus) {
        guildRepository.updateDmgBonus(id, newDamageBonus);
    }
    
    public void updateGuildBonusTavernQuests(int id, int newQuestBonus) {
        guildRepository.updateQuestBonus(id, newQuestBonus);
    }
}
