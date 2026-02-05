package com.kuk.sfgame.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kuk.sfgame.model.Player;

public class PlayerRowMapper implements RowMapper<Player> {
    
    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
        Player player = new Player();
        player.setId(rs.getInt("id"));
        player.setName(rs.getString("name"));
        player.setLevel(rs.getInt("lvl"));
        player.setExperience(rs.getInt("xp"));
        player.setGold(rs.getInt("gold"));
        // Use the actual database column names
        player.setStrength(rs.getInt("base_strength"));
        player.setConstitution(rs.getInt("base_constitution"));
        player.setLuck(rs.getInt("base_luck"));
        // Set position if column exists (for legacy leaderboard)
        try {
            int position = rs.getInt("position");
            player.setPosition(position);
        } catch (SQLException e) {
            // Position column doesn't exist, ignore
        }


        try {
            String guild = rs.getString("guild_name");
            player.setGuild(guild);
        } catch (SQLException e) {
            // Guild column doesn't exist, ignore
        }
        return player;
    }
}
