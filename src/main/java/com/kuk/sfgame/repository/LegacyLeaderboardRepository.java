package com.kuk.sfgame.repository;

import com.kuk.sfgame.model.LegacyLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LegacyLeaderboardRepository extends JpaRepository<LegacyLeaderboard, Integer> {

    List<LegacyLeaderboard> findAllByOrderByPositionAsc();

    @Modifying
    @Query("UPDATE LegacyLeaderboard l SET l.position = :position WHERE l.player.id = :playerId")
    void updatePosition(@Param("playerId") int playerId, @Param("position") int position);

    LegacyLeaderboard findByPlayerId(int playerId);
}
