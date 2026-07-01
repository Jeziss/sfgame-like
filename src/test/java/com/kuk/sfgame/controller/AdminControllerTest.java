package com.kuk.sfgame.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.repository.GuildRepository;
import com.kuk.sfgame.repository.PlayerRepository;
import com.kuk.sfgame.service.impl.PlayerService;
import com.kuk.sfgame.service.impl.QuestLocationService;

class AdminControllerTest {

    @Test
    void updatePlayerInAddModeUsesAddFieldValues() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        GuildRepository guildRepository = mock(GuildRepository.class);
        QuestLocationService questLocationService = mock(QuestLocationService.class);
        PlayerService playerService = mock(PlayerService.class);

        Player player = new Player();
        player.setLevel(1);
        player.setExperience(100);
        player.setGold(50);
        player.setEnergy(100);

        when(playerRepository.findById(7)).thenReturn(Optional.of(player));
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdminController controller = new AdminController(
                playerRepository,
                guildRepository,
                questLocationService,
                playerService
        );

        String view = controller.updatePlayer(
                7,
                "add",
                null,
                null,
                null,
                20,
                20,
                null,
                null,
                null,
                null
        );

        assertEquals("redirect:/admin", view);
        assertEquals(70, player.getGold());
        assertEquals(120, player.getExperience());
    }
}
