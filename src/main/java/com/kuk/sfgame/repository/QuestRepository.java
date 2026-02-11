package com.kuk.sfgame.repository;

import com.kuk.sfgame.model.Quest;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestRepository extends JpaRepository<Quest, Integer> {

    Optional<Quest> findByPlayerId(int playerId);

}
