package com.kuk.sfgame.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.dto.PlayerDto;
import com.kuk.sfgame.mapper.PlayerRowMapper;

@Repository
public class PlayerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Properties sqlQueries = new Properties();

    public void setSQLQueriesFileName(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            sqlQueries.load(fis);
        }
    }

    public List<PlayerDto> findAllPlayersNamesId() {
        String sql = sqlQueries.getProperty("findPlayersNamesIdOrderByName");
        return jdbcTemplate.query(sql, new RowMapper<PlayerDto>() {
            @Override
            public PlayerDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                PlayerDto playerDto = new PlayerDto();
                playerDto.setId(rs.getInt("id"));
                playerDto.setName(rs.getString("name"));
                return playerDto;
            }
        });
    }

    public Player findPlayerByName(String playerName) {
        String sql = sqlQueries.getProperty("findPlayerByName");
        
        List<Player> players = jdbcTemplate.query(sql, new PlayerRowMapper(), playerName);
        return players.isEmpty() ? null : players.get(0);
    }

    public Player findPlayerById(int id) {
        String sql = sqlQueries.getProperty("findPlayerById");
        List<Player> players = jdbcTemplate.query(sql, new PlayerRowMapper(), id);
        return players.isEmpty() ? null : players.get(0);
    }


    // -------- GUILD QUERIES --------------

    public List<Player> findPlayersByGuildId(int guildId) {
        String sql = sqlQueries.getProperty("findPlayersByGuildId");
        return jdbcTemplate.query(sql, new PlayerRowMapper(), guildId);
    }

    public List<Player> findPlayersWithGuild() {
        String sql = sqlQueries.getProperty("findPlayersWithGuild");
        return jdbcTemplate.query(sql, new PlayerRowMapper());
    }

    //--------- LEGACY LEADERBOARD QUERIES ---------------
    public List<Player> findAllPlayersWithPositionOrdered() {
        String sql = sqlQueries.getProperty("findAllPlayersWithPositionOrdered");
        return jdbcTemplate.query(sql, new PlayerRowMapper());
    }

    //---------- SHOP QUERIES ---------------

    public void updatePlayerGold(int playerId, int newGoldAmount) {
        String sql = sqlQueries.getProperty("updatePlayerGold");
        jdbcTemplate.update(sql, newGoldAmount, playerId);
    }
}
