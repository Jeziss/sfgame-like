package com.kuk.sfgame.repository;

import com.kuk.sfgame.model.QuestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestHistoryRepository extends JpaRepository<QuestHistory, Integer> {
    List<QuestHistory> findAllByOrderByCompletedAtDesc();
    List<QuestHistory> findAllByPlayer_NameContainingIgnoreCaseOrderByCompletedAtDesc(String playerName);
    List<QuestHistory> findAllBySuccessOrderByCompletedAtDesc(boolean success);
    List<QuestHistory> findAllByPlayer_NameContainingIgnoreCaseAndSuccessOrderByCompletedAtDesc(String playerName, boolean success);
}
