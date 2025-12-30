package com.kuk.sfgame.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.PlayerClass;

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
        player.setConstitution(rs.getInt("base_constitution"));
        player.setStrength(rs.getInt("base_strength"));
        player.setDexterity(rs.getInt("base_dexterity"));
        player.setIntelligence(rs.getInt("base_intelligence"));
        // player_class is an enum type, need to get it as string
        // Try getting it as string first (if cast in query), otherwise as Object and convert
        String classString = null;
        try {
            classString = rs.getString("player_class");
        } catch (SQLException e) {
            // If direct string access fails, try getting as Object
            Object classObj = rs.getObject("player_class");
            if (classObj != null) {
                classString = classObj.toString();
            }
        }
        if (classString != null) {
            player.setPlayerClass(PlayerClass.valueOf(classString));
        }
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
