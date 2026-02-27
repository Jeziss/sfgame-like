package com.kuk.sfgame.repository;

import com.kuk.sfgame.model.Guild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GuildRepository extends JpaRepository<Guild, Integer> {

    // Najde guildu podle ID
    Guild findById(int id);

    // Najde guildu podle ID hráče
    @Query("""
        SELECT g FROM Guild g
        JOIN g.members m
        WHERE m.id = :playerId
    """)
    Guild findByPlayerId(@Param("playerId") int playerId);

    // Aktualizace všech bonusů najednou
    @Modifying
    @Transactional
    @Query("""
        UPDATE Guild g
        SET g.goldBonusPercent = :gold,
            g.xpBonusPercent = :xp,
            g.hpBonusPercent = :hp,
            g.dmgBonusPercent = :dmg,
            g.questOfferNumber = :quests
        WHERE g.id = :guildId
    """)
    int updateGuildBonuses(
            @Param("guildId") int guildId,
            @Param("gold") int gold,
            @Param("xp") int xp,
            @Param("hp") int hp,
            @Param("dmg") int dmg,
            @Param("quests") int quests
    );

    // Individuální aktualizace bonusů (pokud chceš mít samostatně)
    @Modifying
    @Transactional
    @Query("UPDATE Guild g SET g.goldBonusPercent = :gold WHERE g.id = :guildId")
    int updateGoldBonus(@Param("guildId") int guildId, @Param("gold") int gold);

    @Modifying
    @Transactional
    @Query("UPDATE Guild g SET g.xpBonusPercent = :xp WHERE g.id = :guildId")
    int updateXpBonus(@Param("guildId") int guildId, @Param("xp") int xp);

    @Modifying
    @Transactional
    @Query("UPDATE Guild g SET g.hpBonusPercent = :hp WHERE g.id = :guildId")
    int updateHpBonus(@Param("guildId") int guildId, @Param("hp") int hp);

    @Modifying
    @Transactional
    @Query("UPDATE Guild g SET g.dmgBonusPercent = :dmg WHERE g.id = :guildId")
    int updateDmgBonus(@Param("guildId") int guildId, @Param("dmg") int dmg);

    @Modifying
    @Transactional
    @Query("UPDATE Guild g SET g.questOfferNumber = :quests WHERE g.id = :guildId")
    int updateQuestBonus(@Param("guildId") int guildId, @Param("quests") int quests);
}