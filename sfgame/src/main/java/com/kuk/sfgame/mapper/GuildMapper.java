package com.kuk.sfgame.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.kuk.sfgame.model.Guild;




public class GuildMapper implements RowMapper<Guild> {

    @Override
    public Guild mapRow(ResultSet rs, int rowNum) throws SQLException {
        Guild guild = new Guild();
        guild.setId(rs.getInt("id"));
        guild.setName(rs.getString("name"));
        guild.setXpBonusPercent(rs.getInt("xp_bonus_percent"));
        guild.setGoldBonusPercent(rs.getInt("gold_bonus_percent"));
        guild.setDmgBonusPercent(rs.getInt("dmg_bonus_percent"));
        guild.setHpBonusPercent(rs.getInt("hp_bonus_percent"));
        return guild;
    }
    
}
