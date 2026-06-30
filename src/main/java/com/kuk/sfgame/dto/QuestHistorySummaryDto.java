package com.kuk.sfgame.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record QuestHistorySummaryDto(
        LocalDate day,
        long totalEnergy,
        long totalGold,
        long totalXp,
        BigDecimal rewardPerEnergy,
        BigDecimal xpPerEnergy
) {}
