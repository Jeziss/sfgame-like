package com.kuk.sfgame.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kuk.sfgame.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @EntityGraph(attributePaths = {
        "template",
        "weapon"
    })
    List<Item> findByPlayerId(int playerId);
}

