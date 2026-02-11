package com.kuk.sfgame.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kuk.sfgame.dto.PlayerDto;
import com.kuk.sfgame.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    // ---------------- Basic finders ----------------

    // Najde hráče podle jména
    Optional<Player> findByName(String name);

    // Všichni hráči seřazení podle jména
    List<Player> findAllByOrderByNameAsc();

    // ---------------- Guild related ----------------
    List<Player> findByGuildId(int guildId);

    // ---------------- Leaderboard / position ----------------
    // Transient pole `position` → řadíme v Java
    default List<Player> sortPlayersByPosition(List<Player> players) {
        players.sort((p1, p2) -> Integer.compare(p1.getPosition(), p2.getPosition()));
        return players;
    }

    // ---------------- Update methods ----------------
    // Gold update
    default void updatePlayerGold(Player player, int newGoldAmount) {
        player.setGold(newGoldAmount);
        save(player);
    }

    // Stats update (strength, constitution, luck)
    default void updatePlayerStats(Player player, int strength, int constitution, int luck) {
        player.setStrength(strength);
        player.setConstitution(constitution);
        player.setLuck(luck);
        save(player);
    }

    // Position update (transient, jen pro runtime / memory)
    default void updatePlayerPosition(Player player, int newPosition) {
        player.setPosition(newPosition);
        // Pokud chceš persistovat do DB, musíš pole uložit do DB a nechat ho nepřetransient
    }

    // ---------------- DTO projections ----------------
    // Pokud potřebuješ pouze id + jméno
    default List<PlayerDto> findAllPlayersNamesId() {
        return findAllByOrderByNameAsc().stream()
                .map(p -> new PlayerDto(p.getId(), p.getName()))
                .toList();
    }
}
