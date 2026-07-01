package com.kuk.sfgame.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuk.sfgame.model.QuestLocation;

public interface QuestLocationRepository extends JpaRepository<QuestLocation, Integer> {
    Optional<QuestLocation> findByNameIgnoreCase(String name);
    List<QuestLocation> findAllByOrderByNameAsc();
    boolean existsByNameIgnoreCase(String name);
}
