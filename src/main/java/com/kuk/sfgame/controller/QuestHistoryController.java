package com.kuk.sfgame.controller;

import com.kuk.sfgame.service.impl.QuestService;
import com.kuk.sfgame.model.QuestHistory;
import com.kuk.sfgame.dto.QuestHistorySummaryDto;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QuestHistoryController {

    private final QuestService questService;

    public QuestHistoryController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping("/quest-history")
    public String getQuestHistory(
            @RequestParam(required = false) String playerName,
            @RequestParam(required = false, defaultValue = "ALL") String success,
            @RequestParam(required = false) String date,
            Model model
    ) {
        LocalDate parsedDate = null;
        if (date != null && !date.isBlank()) {
            parsedDate = LocalDate.parse(date);
        }

        List<QuestHistory> history = questService.getQuestHistory(playerName, success, parsedDate);
        List<QuestHistorySummaryDto> summary = questService.getQuestHistorySummary(parsedDate);

        model.addAttribute("history", history);
        model.addAttribute("summary", summary);
        model.addAttribute("playerName", playerName == null ? "" : playerName);
        model.addAttribute("success", success);
        model.addAttribute("date", date == null ? "" : date);

        return "quest-history";
    }
}
